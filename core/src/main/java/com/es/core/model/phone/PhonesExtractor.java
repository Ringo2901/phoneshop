package com.es.core.model.phone;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PhonesExtractor implements ResultSetExtractor<List<Phone>> {
    @Override
    public List<Phone> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Phone> phoneList = new ArrayList<>();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            Phone existingPhone = findPhoneById(phoneList, id);

            if (existingPhone != null) {
                Color color = new Color(resultSet.getLong("colorId"), resultSet.getString("code"));
                existingPhone.getColors().add(color);
            } else {
                Phone phone = new Phone(id,
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("imageUrl"));
                Color color = new Color(resultSet.getLong("colorId"), resultSet.getString("code"));
                phone.setColors(new HashSet<Color>());
                phone.getColors().add(color);
                phoneList.add(phone);
            }
        }
        return phoneList;
    }

    private Phone findPhoneById(List<Phone> phoneList, Long id) {
        for (Phone phone : phoneList) {
            if (phone.getId().equals(id)) {
                return phone;
            }
        }
        return null;
    }

}
