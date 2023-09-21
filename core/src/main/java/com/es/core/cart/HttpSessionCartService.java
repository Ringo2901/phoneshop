package com.es.core.cart;

import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.stock.StockDao;
import com.es.core.order.OutOfStockException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private HttpSession httpSession;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;
    private static final String CART_SESSION_ATTRIBUTE = "cart";

    @Override
    public Cart getCart() {
        Cart cart = (Cart) httpSession.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            httpSession.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) throws OutOfStockException {
        Cart cart = getCart();
        CartItem item = cart.findItemById(phoneId).orElse(null);
        Long stock = stockDao.availableStock(phoneId).longValue();
        if (item == null) {
            if (stock - quantity < 0)
                throw new OutOfStockException("Available " + stock);
            item = new CartItem();
            item.setPhone(phoneDao.get(phoneId).orElse(null));
            item.setQuantity(quantity);
            cart.getItems().add(item);
        } else {
            if (stock - (quantity + item.getQuantity()) < 0)
                throw new OutOfStockException("Available " + (stock - item.getQuantity()));
            item.setQuantity(item.getQuantity() + quantity);
        }
        recalculateCart(cart);
    }

    @Override
    public void update(Map<Long, Long> items) throws OutOfStockException {
        Cart cart = getCart();
        for (CartItem item : cart.getItems()) {
            Long phoneId = item.getPhone().getId();
            Long quantity = items.get(phoneId);
            Long stock = stockDao.availableStock(phoneId).longValue();
            if (stock - quantity < 0) {
                throw new OutOfStockException("Available " + stock);
            }
            item.setQuantity(quantity);
        }
        recalculateCart(cart);
    }


    @Override
    public void remove(Long phoneId) {
        Cart cart = getCart();
        cart.getItems().removeIf(item -> phoneId.equals(item.getPhone().getId()));
        recalculateCart(cart);
    }

    @Override
    public long getTotalQuantity() {
        return getCart().getTotalQuantity();
    }

    @Override
    public BigDecimal getTotalCost() {
        return getCart().getTotalCost();
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .collect(Collectors.summingLong(q -> q.longValue()))
        );

        cart.setTotalCost(cart.getItems().stream()
                .map(item -> {
                    if (item.getPhone().getPrice() == null)
                        return BigDecimal.ZERO;
                    else return item.getPhone().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
