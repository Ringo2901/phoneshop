package com.es.phoneshop.web.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/signIn")
public class LoginPageController {
    @GetMapping
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/userMiniStatus")
    public String getUserMiniStatus() {
        return "userMiniStatus";
    }
}
