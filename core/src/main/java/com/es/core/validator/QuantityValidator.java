package com.es.core.validator;

import com.es.core.cart.CartItemDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class QuantityValidator implements ConstraintValidator<QuantityNotNul, CartItemDto> {
    @Override
    public void initialize(QuantityNotNul quantityNotNul) {
    }

    @Override
    public boolean isValid(CartItemDto item, ConstraintValidatorContext constraintValidatorContext) {
        if (item.getQuantity() == null && item.getPhoneId() != null) {
            return false;
        }
        return true;
    }
}