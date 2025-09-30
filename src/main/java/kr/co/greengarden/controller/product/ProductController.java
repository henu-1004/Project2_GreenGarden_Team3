package kr.co.greengarden.controller.product;

/*
    날짜 : 2025/09/26
    이름 : 이수연
    내용 : 상품 컨트롤러
*/

import kr.co.greengarden.entity.Product;
import kr.co.greengarden.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/list")
    public String productListPage() {
        return "product/list";
    }

    @GetMapping("/product/list2")
    public String productListPage2(Model model) {

        List<Product> productList = productService.getAllProducts();

        model.addAttribute("productList", productList);

        return "product/list2";
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

    @GetMapping("/product/search")
    public String searchPage() {
        return "product/search";
    }

}
