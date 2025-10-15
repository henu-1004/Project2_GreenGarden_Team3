package kr.co.greengarden.controller.product;

/*
    날짜 : 2025/09/26 & 2025/10/2
    이름 : 이수연 & 한탁원
    내용 : 상품 컨트롤러 & 기능
*/
import kr.co.greengarden.dto.CartDTO;
import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.dto.OrderInfoDTO;
import kr.co.greengarden.dto.ProductListDTO;
import kr.co.greengarden.dto.admin.AdminProductListDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Order;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.security.MemberDetails;
import kr.co.greengarden.service.CartService;
import kr.co.greengarden.service.MemberService;
import kr.co.greengarden.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class    ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final MemberService memberService;

    @GetMapping("/product/list")
    public String productListPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "proId") String sortBy,
                                   @RequestParam(defaultValue = "desc") String direction,
                                   @RequestParam(defaultValue = "plants") String slug,
                                   Model model) {

        Page<ProductListDTO> productList = productService.getProductCards(page, sortBy, direction, slug);

        for (ProductListDTO p : productList) {
            int original = (int) Math.ceil(p.getPrice() / (1 - (p.getDiscountRate() / 100.0)));
            p.setOriginalPrice(original);
        }

        model.addAttribute("productList", productList);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("slug", slug);

        return "product/list";
    }

    @GetMapping("/product/view")
    public String productViewPage(@RequestParam String proId, Model model) {
        Product product = productService.getProduct(Integer.parseInt(proId));
        productService.updateViewProduct(Integer.parseInt(proId));

        model.addAttribute("product", product);

        return "product/view";
    }

    @GetMapping("/product/cart")
    public String cartPage2(@RequestParam(defaultValue = "0") int page, @AuthenticationPrincipal MemberDetails memberDetails, Model model, RedirectAttributes ra) {
        Page<CartListDTO> cartList = cartService.getCartPage(memberDetails.getUsername(), page, 5);

        for (CartListDTO c : cartList) {
            int original = (int) Math.ceil(c.getPrice() / (1 - (c.getDiscountRate() / 100.0)));
            c.setOriginalPrice(original);
        }

        model.addAttribute("cartList", cartList);

        return "product/cart";
    }

    @GetMapping("/product/order")
    public String orderPage(){
        return "product/order";
    }

    @PostMapping("/product/order2")
    public String order(@RequestParam("cartIds") List<Integer> cartIds,
                        Model model) {
        List<CartListDTO> cartList = cartService.getCartListBycartIds(cartIds);

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

        int count = 0;
        int originalTotalPrice = 0;
        int totalPrice = 0;
        int discountPrice = 0;
        int deliveryFee = 0;
        int totalPoint = 0;

        for (CartListDTO c : cartList) {
            int original = (int) Math.ceil(c.getPrice() / (1 - (c.getDiscountRate() / 100.0)));
            c.setOriginalPrice(original);

            count += c.getQuantity();
            originalTotalPrice += original * count;
            discountPrice += (c.getPrice() - original) * count;
            if(deliveryFee < c.getDeliveryFee()) {
                deliveryFee = c.getDeliveryFee();
            }
            totalPoint += c.getPoint() * count;
            totalPrice += c.getPrice() * count;
        }

        totalPrice += deliveryFee;

        orderInfoDTO.setCount(count);
        orderInfoDTO.setOriginalTotalPrice(originalTotalPrice);
        orderInfoDTO.setTotalPrice(totalPrice);
        orderInfoDTO.setDiscountPrice(discountPrice);
        orderInfoDTO.setDeliveryFee(deliveryFee);
        orderInfoDTO.setTotalPoint(totalPoint);

        model.addAttribute("orderInfo", orderInfoDTO);
        model.addAttribute("cartList", cartList);

        return "product/order2";
    }

    @GetMapping("/product/order2")
    public String orderPage(@RequestParam String cartId, Model model) {
        List<CartListDTO> cartList = cartService.getCartList(Integer.parseInt(cartId));

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

        int count = 0;
        int originalTotalPrice = 0;
        int totalPrice = 0;
        int discountPrice = 0;
        int deliveryFee = 0;
        int totalPoint = 0;

        for (CartListDTO c : cartList) {
            int original = (int) Math.ceil(c.getPrice() / (1 - (c.getDiscountRate() / 100.0)));
            c.setOriginalPrice(original);

            count += c.getQuantity();
            originalTotalPrice += original * count;
            discountPrice += (c.getPrice() - original) * count;
            if(deliveryFee < c.getDeliveryFee()) {
                deliveryFee = c.getDeliveryFee();
            }
            totalPoint += c.getPoint() * count;
            totalPrice += c.getPrice() * count;
        }

        totalPrice += deliveryFee;

        orderInfoDTO.setCount(count);
        orderInfoDTO.setOriginalTotalPrice(originalTotalPrice);
        orderInfoDTO.setTotalPrice(totalPrice);
        orderInfoDTO.setDiscountPrice(discountPrice);
        orderInfoDTO.setDeliveryFee(deliveryFee);
        orderInfoDTO.setTotalPoint(totalPoint);

        model.addAttribute("orderInfo", orderInfoDTO);
        model.addAttribute("cartList", cartList);

        return "product/order2";
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
