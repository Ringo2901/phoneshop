package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItemDto;
import com.es.core.cart.CartItemsUpdateDto;
import com.es.core.cart.CartService;
import com.es.core.order.OutOfStockException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(@ModelAttribute("cartItemsQuantities") CartItemsUpdateDto dto) {
        dto.copyFromCart(cartService.getCart());
        return "cart";
    }

   /* @RequestMapping(method = RequestMethod.PUT)
    public String updateCart(@ModelAttribute("cartItemsQuantities") @Valid CartItemsUpdateDto dto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "There was some errors while updating");
        }
                for (CartItemDto cartItem : dto.getItems()) {
                    try {
                        cartService.update(Collections.singletonMap(cartItem.getPhoneId(), cartItem.getQuantity()));

                    } catch (OutOfStockException e) {
                        model.addAttribute("phoneError_" + cartItem.getPhoneId(), "Out of stock error: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        model.addAttribute("phoneError_" + cartItem.getPhoneId(), "Invalid number format: " + e.getMessage());
                    }
                }
                model.addAttribute("successMessage", "Cart successfully updated");
        dto.copyFromCart(cartService.getCart());
        return "cart";
    }*/

    @RequestMapping(method = RequestMethod.PUT)
    public String updateCart(@ModelAttribute("cartItemsQuantities") @Valid CartItemsUpdateDto dto,
                             BindingResult bindingResult, Model model) {
        List<String> validationErrors = new ArrayList<>(Collections.nCopies(dto.getItems().size(), null));
        ;
        List<String> outOfStockErrors = new ArrayList<>(Collections.nCopies(dto.getItems().size(), null));
        ;
        boolean hasErrors = false;
        int i = 0;
        if (bindingResult.hasErrors()) {
            for (CartItemDto cartItemDto : dto.getItems()) {
                if (bindingResult.hasFieldErrors("items[" + i + "].quantity")) {
                    validationErrors.set(i, bindingResult.getFieldError("items[" + i + "].quantity").getDefaultMessage());
                    hasErrors = true;
                }
                i++;
            }
        }
        i = 0;
        for (CartItemDto cartItem : dto.getItems()) {
            if (validationErrors.get(i) == null) {
                try {
                    cartService.update(cartItem.getPhoneId(), cartItem.getQuantity());
                } catch (OutOfStockException e) {
                    outOfStockErrors.set(i, "Out of stock error - " + e.getMessage());
                    hasErrors = true;
                }
            }
            i++;
        }
        dto.copyFromCart(cartService.getCart());
        model.addAttribute("validationErrors", validationErrors);
        model.addAttribute("outOfStockErrors", outOfStockErrors);

        if (hasErrors) {
            model.addAttribute("errorMessage", "There was some errors during updating");
        } else {
            model.addAttribute("successMessage", "Cart successfully updated");
        }

        return "cart";
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteFromCart(@RequestParam("phoneId") Long phoneId, Model model) {
        cartService.remove(phoneId);
        CartItemsUpdateDto dto = new CartItemsUpdateDto();
        dto.copyFromCart(cartService.getCart());
        model.addAttribute("cartItemsQuantities", dto);
        model.addAttribute("successMessage", "Successfully deleted from cart");
        return "cart";
    }

    @ModelAttribute("cart")
    public Cart cartAttribute() {
        return cartService.getCart();
    }
}
