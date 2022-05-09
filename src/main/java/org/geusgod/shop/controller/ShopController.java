package org.geusgod.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {

    @GetMapping("/")
    public String mainUrl(Model model) {
        model.addAttribute("data", "Hello~ world~~");
        return "main-page";
    }
}
