package com.es.core.cart;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class CartItemDto {
    private Long phoneId;
    @NotNull(message = "Quantity was empty")
    @Min(value = 1, message = "Quantity must be more then 0")
    @Digits(integer = 10, fraction = 0, message = "Quantity must be a number")
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
