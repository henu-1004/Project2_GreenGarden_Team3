package kr.co.greengarden.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
    @GetMapping("/product/list")
    public String productListPage() {
        return "product/list";
    }

    @GetMapping("/product/view")
    public String productViewPage() {
        return "product/view";
    }
}
