package com.es.phoneshop.web.validator;

import com.es.core.cart.CartItemDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
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
        if (cartItemDto.getQuantity() == null) {
            errors.rejectValue("quantity", "empty value", "Quantity is empty");
        } else if (cartItemDto.getQuantity() <= 0) {
            errors.rejectValue("quantity", "negative value", "Quantity must be more then 0");
        }
    }
}
