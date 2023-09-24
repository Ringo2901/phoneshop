package com.es.core.cart;

import com.es.core.order.OutOfStockException;

import java.math.BigDecimal;
import java.util.Map;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity) throws OutOfStockException;

    /**
     * @param items
     * key: {@link com.es.core.model.phone.Phone#id}
     * value: quantity
     */
    void update(Long phoneId, Long phoneQuantity);

    void remove(Long phoneId);
    long getTotalQuantity();

    BigDecimal getTotalCost();
}
