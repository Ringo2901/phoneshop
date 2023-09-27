package com.es.core.order;

import com.es.core.model.order.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class JdbcOrderItemDao implements OrderItemDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private OrderItemsExtractor orderItemsExtractor;

    private static final String GET_ORDER_ITEMS = "SELECT * FROM order2item WHERE orderId = ?";

    @Override
    public List<OrderItem> getOrderItems(Long key) {
        return jdbcTemplate.query(GET_ORDER_ITEMS, orderItemsExtractor, key);
    }
}