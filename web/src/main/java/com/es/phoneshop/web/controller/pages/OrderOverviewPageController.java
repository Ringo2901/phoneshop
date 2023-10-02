package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.OrderDao;
import com.es.core.model.phone.JdbcPhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private OrderDao orderDao;

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable("id") String orderId, Model model) {
        model.addAttribute("order", orderDao.getBySecureID(orderId).orElse(null));
        return "orderOverview";
    }
}
