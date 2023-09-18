package com.es.phoneshop.web.validator;

import com.es.core.cart.CartItemDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class QuantityValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CartItemDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CartItemDto cartItemDto = (CartItemDto) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "empty value", "Quantity is empty");
        if (cartItemDto.getQuantity() != null && !isNumeric(cartItemDto.getQuantity())) {
            errors.rejectValue("quantity", "non-numeric value", "Quantity must be a number");
        } else {
            if (cartItemDto.getQuantity() != null && Long.parseLong(cartItemDto.getQuantity()) <= 0) {
                errors.rejectValue("quantity", "negative value", "Quantity must be more than 0");
            }
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
