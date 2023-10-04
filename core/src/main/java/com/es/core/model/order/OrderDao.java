package com.es.core.model.order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> getById(Long key);

    Optional<Order> getBySecureID(String secureID);

    List<Order> findOrders();

    void save(Order order);

    void changeStatus(Long id, OrderStatus status);
}
