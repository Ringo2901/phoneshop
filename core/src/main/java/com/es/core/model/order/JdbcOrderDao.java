package com.es.core.model.order;

import com.es.core.order.OrdersExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class JdbcOrderDao implements OrderDao {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private OrdersExtractor ordersExtractor;

    private static final String GET_ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?";
    private static final String GET_ORDER_BY_SECURE_ID = "SELECT * FROM orders WHERE secureID = ?";
    private static final String SAVE_ORDER = "INSERT INTO orders (secureID, subtotal, deliveryPrice, " +
            "totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, additionalInformation) " +
            "VALUES (:secureID, :subtotal, :deliveryPrice, :totalPrice, :firstName, :lastName, " +
            ":deliveryAddress, :contactPhoneNo, :additionalInformation)";
    private static final String CHANGE_STATUS = "UPDATE orders SET status = ? WHERE id = ?";
    private static final String ADD_ORDER2ITEM = "INSERT INTO order2item (orderId, phoneId, quantity) " +
            "VALUES (?, ?, ?)";

    @Override
    public Optional<Order> getById(final Long key) {
        return namedParameterJdbcTemplate.getJdbcOperations()
                .query(GET_ORDER_BY_ID, ordersExtractor, key).stream().findAny();
    }

    @Override
    public Optional<Order> getBySecureID(String secureID) {
        return namedParameterJdbcTemplate.getJdbcOperations()
                .query(GET_ORDER_BY_SECURE_ID, ordersExtractor, secureID).stream().findAny();
    }

    @Override
    public void save(final Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SAVE_ORDER, new BeanPropertySqlParameterSource(order), keyHolder);
        Long id = keyHolder.getKey().longValue();
        namedParameterJdbcTemplate.getJdbcOperations()
                .update(CHANGE_STATUS, order.getStatus().toString(), id);
        order.setId(id);
        order.getOrderItems().stream()
                .forEach(item -> namedParameterJdbcTemplate.getJdbcOperations()
                        .update(ADD_ORDER2ITEM, id, item.getPhone().getId(), item.getQuantity()));
    }
}