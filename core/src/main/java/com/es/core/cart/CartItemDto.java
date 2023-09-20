package com.es.core.cart;

import com.es.core.validator.QuantityNotNul;

import javax.validation.constraints.Min;

@QuantityNotNul(message = "Quantity was empty")
public class CartItemDto {
    private Long phoneId;
    @Min(value = 1, message = "Quantity must be more then 0")
    private Long quantity;

    public CartItemDto() {
    }

    public CartItemDto(Long phoneId, Long quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
