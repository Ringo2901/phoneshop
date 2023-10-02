package com.es.core.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderItemDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrdersExtractor implements ResultSetExtractor<List<Order>> {
    @Resource
    private OrderItemDao jdbcOrderItemDao;

    @Override
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            Order order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setSecureID(resultSet.getString("secureID"));
            order.setSubtotal(resultSet.getBigDecimal("subtotal"));
            order.setDeliveryPrice(resultSet.getBigDecimal("deliveryPrice"));
            order.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
            order.setFirstName(resultSet.getString("firstName"));
            order.setLastName(resultSet.getString("lastName"));
            order.setDeliveryAddress(resultSet.getString("deliveryAddress"));
            order.setContactPhoneNo(resultSet.getString("contactPhoneNo"));
            order.setAdditionalInformation(resultSet.getString("additionalInformation"));
            order.setStatus(OrderStatus.fromString(resultSet.getString("status")));
            order.setOrderItems(jdbcOrderItemDao.getOrderItems(order.getId()));
            orders.add(order);
        }
        return orders;
    }
}
