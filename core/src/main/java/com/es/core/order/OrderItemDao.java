package com.es.core.order;

import com.es.core.model.order.OrderItem;

import java.util.List;

public interface OrderItemDao {
    List<OrderItem> getOrderItems (Long key);
}
