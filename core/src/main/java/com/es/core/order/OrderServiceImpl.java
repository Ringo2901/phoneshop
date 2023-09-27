package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.stock.StockDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private StockDao stockDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private CartService cartService;
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setDeliveryPrice(deliveryPrice);
        order.setSubtotal(cart.getTotalCost());
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        fillOrderItems(order, cart);
        return order;
    }

    @Override
    @Transactional
    public void placeOrder(final Order order) throws OutOfStockException {
        checkStock(order);
        order.setStatus(OrderStatus.NEW);
        order.getOrderItems().stream()
                .forEach(item -> stockDao.reserve(item.getPhone().getId(), item.getQuantity()));
        order.setSecureID(UUID.randomUUID().toString());
        orderDao.save(order);
        cartService.clear();
    }

    private void fillOrderItems(Order order, Cart cart) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        cart.getItems().stream().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setPhone(cartItem.getPhone());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        });
        order.setOrderItems(orderItems);
    }

    private void checkStock(final Order order) throws OutOfStockException {
        List<OrderItem> outOfStockItems = order.getOrderItems().stream()
                .filter(item -> stockDao.availableStock(item.getPhone().getId()) - item.getQuantity() < 0)
                .collect(Collectors.toList());
        if (!outOfStockItems.isEmpty()) {
            StringBuilder outOfStockModels = new StringBuilder();
            outOfStockItems.stream().forEach(item -> {
                outOfStockModels.append(item.getPhone().getModel() + "; ");
                cartService.remove(item.getPhone().getId());
            });
            throw new OutOfStockException("Some of items out of stock (" + outOfStockModels + "). They deleted from cart.");
        }
    }
}
