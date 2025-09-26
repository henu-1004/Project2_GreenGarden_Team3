package kr.co.greengarden.controller.product;

/*
    날짜 : 2025/09/26
    이름 : 이수연
    내용 : 상품 컨트롤러
*/

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

    @GetMapping("/product/cart")
    public String cartPage() {
        return "product/cart";
    }

    @GetMapping("/product/order")
    public String orderPage() {
        return "product/order";
    }

    @GetMapping("/product/complete")
    public String completePage() {
        return "product/complete";
    }

}
