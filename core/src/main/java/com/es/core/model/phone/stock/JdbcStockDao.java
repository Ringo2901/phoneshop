package com.es.core.model.phone.stock;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class JdbcStockDao implements StockDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private StockExtractor stockExtractor;
    private static final String GET_STOCK_BY_ID = "SELECT * FROM stocks WHERE phoneId = ?";

    @Override
    public Integer availableStock(Long phoneId) {
        Stock stock = jdbcTemplate.query(GET_STOCK_BY_ID, new Object[]{phoneId}, stockExtractor).get(0);
        return stock.getStock() - stock.getReserved();
    }
}
