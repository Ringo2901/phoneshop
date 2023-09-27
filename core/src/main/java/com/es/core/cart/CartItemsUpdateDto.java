package com.es.core.cart;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class CartItemsUpdateDto {
    @Valid
    private List<CartItemDto> items;

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public void copyFromCart(Cart cart) {
        items = new ArrayList<CartItemDto>();
        cart.getItems().stream()
                .forEach(cartItem -> items.add(new CartItemDto(cartItem.getPhone().getId(), cartItem.getQuantity())));
    }
}
