package com.es.phoneshop.web.controller;

import com.es.core.cart.CartAddDto;
import com.es.core.cart.CartItemDto;
import com.es.core.cart.CartService;
import com.es.phoneshop.web.validator.QuantityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;
    @Autowired
    private QuantityValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public CartAddDto addPhone(@Validated @RequestBody CartItemDto cartItem,
                               BindingResult bindingResult) {
        CartAddDto message = new CartAddDto();
        if (!bindingResult.hasErrors()) {
            cartService.addPhone(cartItem.getPhoneId(), Long.parseLong(cartItem.getQuantity()));
            message.setMessage("Successfully added to cart");
            message.setErrorStatus(false);
        } else {
            message.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            message.setErrorStatus(true);
        }
        message.setPhoneId(cartItem.getPhoneId());
        message.setTotalCost(cartService.getTotalCost());
        message.setTotalQuantity(cartService.getTotalQuantity());
        return message;
    }
}
