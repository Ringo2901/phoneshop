package com.es.core.cart;

import java.util.List;

public class QuickAddDto {
    private List<QuickAddItemDto> items;

    public QuickAddDto() {
    }

    public QuickAddDto(List<QuickAddItemDto> items) {
        this.items = items;
    }

    public List<QuickAddItemDto> getItems() {
        return items;
    }

    public void setItems(List<QuickAddItemDto> items) {
        this.items = items;
    }
}