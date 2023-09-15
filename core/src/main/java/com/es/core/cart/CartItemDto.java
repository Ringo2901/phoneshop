package com.es.core.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public class CartItemDto {
    @NotNull
    private Long phoneId;
    @NotNull(message = "Quantity is empty")
    @Min(value = 1, message = "Quantity must be more then 0")
    private Long quantity;

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
