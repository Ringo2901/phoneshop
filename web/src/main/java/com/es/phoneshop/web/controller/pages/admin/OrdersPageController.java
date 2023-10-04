package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderService orderService;

    @GetMapping
    public String showOrderList(Model model) {
        model.addAttribute("orders", orderDao.findOrders());
        return "adminOrdersPage";
    }

    @GetMapping("/{id}")
    public String showOrder(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderDao.getById(id).orElse(null));
        return "adminOrderManagePage";
    }

    @PostMapping("/{id}")
    public String changeOrderStatus(@PathVariable("id") Long id,
                                    @RequestParam(value = "status", required = false) String status, Model model) {
        OrderStatus newStatus = OrderStatus.fromString(status);
        if (newStatus != null) {
            orderService.changeOrderStatus(id, OrderStatus.fromString(status));
            model.addAttribute("successMessage", "Successfully change status");
        }
        else {
            model.addAttribute("errorMessage", "There was an error");
        }
        model.addAttribute("order", orderDao.getById(id).orElse(null));
        return "adminOrderManagePage";
    }
}
