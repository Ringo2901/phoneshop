package com.es.core.model.phone.color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class JdbcColorDao implements ColorDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private final ColorExtractor colorExtractor;
    public JdbcColorDao(ColorExtractor colorExtractor) {
        this.colorExtractor = colorExtractor;
    }
    private static final String GET_QUERY = "select COLORS.ID, COLORS.CODE " +
            "from (select * from PHONE2COLOR where PHONEID = ?) p2c " +
            "left join COLORS on p2c.COLORID = COLORS.ID order by COLORS.ID";


    @Override
    public List<Color> getColors(Long id) {
        return jdbcTemplate.query(GET_QUERY, new Object[]{id}, colorExtractor);
    }
}
