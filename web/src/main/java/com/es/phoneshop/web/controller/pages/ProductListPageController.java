package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.enums.SortField;
import com.es.core.enums.SortOrder;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;

    private static final int PHONES_ON_PAGE = 10;

    @GetMapping
    public String showProductList(@RequestParam(name = "page", required = false) Integer pageNumber,
                                  @RequestParam(name = "sort", required = false) String sortField,
                                  @RequestParam(name = "order", required = false) String sortOrder,
                                  @RequestParam(name = "query", required = false) String query,
                                  Model model) {
        List<Phone> phones = phoneDao.findAll(((pageNumber == null ? 1 : pageNumber) - 1) * PHONES_ON_PAGE, PHONES_ON_PAGE,
                SortField.getValue(sortField), SortOrder.getValue(sortOrder), query);
        model.addAttribute("phones", phones);
        Long number = phoneDao.numberByQuery(query);
        model.addAttribute("numberOfPhones", number);
        model.addAttribute("numberOfPages", (number + PHONES_ON_PAGE - 1) / PHONES_ON_PAGE);
        return "productList";
    }

    @ModelAttribute("cart")
    public Cart cartOnPage() {
        return cartService.getCart();
    }
}
