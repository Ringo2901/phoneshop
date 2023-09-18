package com.es.core.cart;

import com.es.core.model.phone.PhoneDao;
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
    public void addPhone(Long phoneId, Long quantity) {
        Cart cart = getCart();
        CartItem item = cart.findItemById(phoneId).orElse(null);
        if (item == null) {
            item = new CartItem();
            item.setPhone(phoneDao.get(phoneId).orElse(null));
            item.setQuantity(quantity);
            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        recalculateCart(cart);
    }

    @Override
    public void update(Map<Long, Long> items) {
        Cart cart = getCart();
        recalculateCart(cart);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void remove(Long phoneId) {
        Cart cart = getCart();
        recalculateCart(cart);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public long getTotalQuantity() {
        Cart cart = getCart();
        return cart.getTotalQuantity();
    }

    @Override
    public BigDecimal getTotalCost() {
        Cart cart = getCart();
        return cart.getTotalCost();
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
