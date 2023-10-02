package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.order.ClientOrderDto;
import com.es.core.order.OrderService;
import com.es.core.order.OutOfStockException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) throws OutOfStockException {
        model.addAttribute("order", orderService.createOrder(cartService.getCart()));
        model.addAttribute("orderDto", new ClientOrderDto());
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@ModelAttribute("orderDto") @Valid ClientOrderDto orderDto,
                             BindingResult bindingResult, Model model) throws OutOfStockException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("order", orderService.createOrder(cartService.getCart()));
            return "order";
        }
        Order order = fillClientData(orderDto);
        try {
            orderService.placeOrder(order);
        } catch (OutOfStockException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            model.addAttribute("order", orderService.createOrder(cartService.getCart()));
            return "order";
        }
        return "redirect:/orderOverview/" + order.getSecureID();
    }

    @ModelAttribute("cart")
    public Cart cartAttribute() {
        return cartService.getCart();
    }

    private Order fillClientData(ClientOrderDto orderDto) {
        Order order = orderService.createOrder(cartService.getCart());
        order.setFirstName(orderDto.getFirstName());
        order.setLastName(orderDto.getLastName());
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        order.setContactPhoneNo(orderDto.getContactPhoneNo());
        order.setAdditionalInformation(orderDto.getAdditionalInformation());
        return order;
    }
}