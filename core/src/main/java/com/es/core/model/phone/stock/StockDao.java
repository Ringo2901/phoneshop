package com.es.core.model.phone.stock;

public interface StockDao {
    Integer availableStock(Long phoneId);
    void reserve(Long phoneId, Long quantity);
}
