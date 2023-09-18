package com.es.core.model.phone.color;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ColorExtractor implements ResultSetExtractor<List<Color>> {

    @Override
    public List<Color> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Color> colors = new ArrayList<>();

        while (resultSet.next()) {
            Color color = new Color();
            color.setId(resultSet.getLong("ID"));
            color.setCode(resultSet.getString("CODE"));
            colors.add(color);
        }
        return colors;
    }
}
