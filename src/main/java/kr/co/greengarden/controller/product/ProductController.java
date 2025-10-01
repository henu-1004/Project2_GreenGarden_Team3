package kr.co.greengarden.controller.product;

/*
    날짜 : 2025/09/26
    이름 : 이수연
    내용 : 상품 컨트롤러
*/
import kr.co.greengarden.dto.CartDTO;
import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.security.MemberDetails;
import kr.co.greengarden.service.CartService;
import kr.co.greengarden.service.MemberService;
import kr.co.greengarden.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final MemberService memberService;

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
    public String productViewPage(@RequestParam String proId, Model model) {

        Optional<Product> optProduct = productService.getProduct(Integer.parseInt(proId));

        if(optProduct.isPresent()) {
            model.addAttribute("product", optProduct.get());
        }

        return "product/view2";
    }

    @GetMapping("/product/cart")
    public String cartPage(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        List<CartListDTO> cartList = cartService.getCartList(memberDetails.getUsername());

        model.addAttribute("cartList", cartList);

        return "product/cart2";
    }

    @GetMapping("/product/order")
    public String orderPage() {
        return "product/order";
    }

    @PostMapping("/product/action")
    public String handleProductAction(@AuthenticationPrincipal MemberDetails memberDetails,
                                      @RequestParam("action") String action,
                                      CartDTO cartDTO) {

        if ("cart".equals(action)) {
            cartDTO.setMemId(memberDetails.getUsername());
            log.info("cartDTO:{}", cartDTO.toString());
            cartService.register(cartDTO);

            return "redirect:/product/cart";

        } else if ("order".equals(action)) {
            // 주문 페이지로 이동
            return "redirect:/product/order";
        }

        return "product/cart";
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
