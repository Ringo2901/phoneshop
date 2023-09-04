package com.es.core.model.phone;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PhoneExtractor implements ResultSetExtractor<List<Phone>> {
    @Override
    public List<Phone> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Phone> phoneList = new ArrayList<>();
        while (resultSet.next()) {
            Phone phone = Phone.builder()
                    .id(resultSet.getLong("id"))
                    .brand(resultSet.getString("brand"))
                    .model(resultSet.getString("model"))
                    .price(resultSet.getBigDecimal("price"))
                    .imageUrl(resultSet.getString("imageUrl"))
                    .build();
            Color color = new Color(resultSet.getLong("colorId"), resultSet.getString("code"));
            Phone existingPhone = phoneList.stream()
                    .filter(p -> p.getId().equals(phone.getId()))
                    .findFirst()
                    .orElse(null);
            if (existingPhone != null) {
                existingPhone.getColors().add(color);
            } else {
                phone.setColors(new HashSet<Color>());
                phone.getColors().add(color);
                phoneList.add(phone);
            }
        }
        return phoneList;
    }
}
