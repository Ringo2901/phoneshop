package com.es.core.cart;

public class QuickAddItemDto {
    private String model;
    private String quantity;

    public QuickAddItemDto() {
    }

    public QuickAddItemDto(String model, String quantity) {
        this.model = model;
        this.quantity = quantity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
