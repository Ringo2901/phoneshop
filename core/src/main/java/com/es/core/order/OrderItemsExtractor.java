package com.es.core.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.PhoneDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
public class OrderItemsExtractor implements ResultSetExtractor<List<OrderItem>> {
    @Resource
    private PhoneDao phoneDao;
    @Override
    public List<OrderItem> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<OrderItem> orderItems = new ArrayList<>();
        while (resultSet.next()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPhone(phoneDao.get(resultSet.getLong("phoneId")).orElse(null));
            orderItem.setQuantity(resultSet.getLong("quantity"));
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
