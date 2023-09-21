package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
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
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(method = RequestMethod.PUT)
    public String updateCart(@ModelAttribute("cartItemsQuantities") @Valid CartItemsUpdateDto dto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "There was some errors while updating");
        } else {
            try {
                cartService.update(makeUpdateMap(dto));
                dto.copyFromCart(cartService.getCart());
                model.addAttribute("successMessage", "Cart successfully updated");
            } catch (OutOfStockException e) {
                model.addAttribute("errorMessage", "Out of stock error: " + e.getMessage());
            } catch (NumberFormatException e) {
                model.addAttribute("errorMessage", "Invalid number format: " + e.getMessage());
            }
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

    private Map<Long, Long> makeUpdateMap(CartItemsUpdateDto dto) {
        Map<Long, Long> map = new HashMap<Long, Long>();
        dto.getItems().stream().forEach(item -> {
            if (item.getPhoneId() != null && item.getQuantity() != null) {
                map.put(item.getPhoneId(), item.getQuantity());
            }
        });
        return map;
    }
}
