package com.es.core.model.order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> getById(Long key);

    Optional<Order> getBySecureID(String secureID);

    void save(Order order);
}
