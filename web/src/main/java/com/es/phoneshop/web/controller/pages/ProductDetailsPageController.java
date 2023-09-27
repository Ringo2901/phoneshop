package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long phoneId, Model model) {
        model.addAttribute("phone", phoneDao.get(phoneId).orElse(null));
        return "productPage";
    }

    @ModelAttribute("cart")
    public Cart cartOnPage() {
        return cartService.getCart();
    }
}
