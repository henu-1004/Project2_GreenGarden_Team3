package kr.co.greengarden.controller.product;

/*
    날짜 : 2025/09/26 & 2025/10/2
    이름 : 이수연 & 한탁원
    내용 : 상품 컨트롤러 & 기능
*/

import kr.co.greengarden.dto.*;
import kr.co.greengarden.dto.admin.AdminOrderListDTO;
import kr.co.greengarden.dto.admin.AdminProductListDTO;
import kr.co.greengarden.entity.*;
import kr.co.greengarden.security.MemberDetails;
import kr.co.greengarden.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final MemberService memberService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

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
    public String orderPage(int productId, Integer quantity, Model model) {
        Product product = productService.getProduct(productId);
        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity); // 선택한 수량 전달

        return "product/order";
    }

    @PostMapping("/product/action")
    public String handleProductAction(@AuthenticationPrincipal MemberDetails memberDetails,
                                      @RequestParam("action") String action,
                                      CartDTO cartDTO) {
        
        // 장바구니 버튼 클릭 시
        if ("cart".equals(action)) {
            cartDTO.setMemId(memberDetails.getUsername());
            // 장바구니에 아무 물품 없을 시, 주문 번호 생성
            cartService.register(cartDTO);

            return "redirect:/product/cart";
        } 
        // 주문하기 버튼 클릭 시
        else if ("order".equals(action)) {
            // 주문 페이지로 이동
            return "redirect:/product/order";
        }

        return "product/cart";
    }

    @PostMapping("/product/order2")
    public String order(@RequestParam("cartIds") List<Integer> cartIds, Model model) {
        List<CartListDTO> cartList = cartService.getCartListBycartIds(cartIds);

        // 삭제, 멤버 주문 번호
        model.addAttribute("orderInfo", orderService.getOrderInfo(cartList));
        model.addAttribute("cartList", cartList);

        return "product/order2";
    }

    @GetMapping("/product/order2")
    public String orderPage(@RequestParam String cartId, Model model) {
        List<CartListDTO> cartList = cartService.getCartList(Integer.parseInt(cartId));

        model.addAttribute("orderInfo", orderService.getOrderInfo(cartList));
        model.addAttribute("cartList", cartList);

        return "product/order2";
    }


    @PostMapping("/product/orderFix")
    public String orderFix(OrderDTO orderDTO, OrderItemListWrapper orderItemList, @AuthenticationPrincipal MemberDetails memberDetails) {
        // 결제 대기 -> 결제 완료

        /*  2025/10/16 한탁원
            1. prodId(전체)
            2. 배송정보 (이름 연락처 주소 우편번호 기본주소 상세주소 배송메모
            3. 최종결제
            4. 최종결제방법
        */
        /* 2025/10/16 한탁원
           1. orderDTO를 이용해서 Order 테이블에 데이터 삽입 (Member Security에서 MemberDetail를 통해 ID 가져옴) -> orderNo 가 생김.
           2. orderNo를 통해 orderItem 테이블에 데이터 삽입
           3.
         */
        orderService.orderRegister(orderDTO, orderItemList, memberDetails);

        return "redirect:/product/complete?orderNo=" + orderDTO.getOrderNo();
    }

    /* 2025/10/17 이수연 */
    @GetMapping("/product/complete")
    public String completePage(@AuthenticationPrincipal MemberDetails memberDetails, @RequestParam String orderNo, Model model) {
        /*
            1. 주문완료된 정보가 그대로 보이도록 하기

            2. 주문완료된 상품은 장바구니에서 지우기
        */


        // - 주문 정보 가져오기 (Order 테이블) findById
        //Order order = orderRepository.findByID(orderNo);
        // - OrderNo - OrderItem 테이블 정보 가져오기
        //List<OrderItem> orderItemList = re.findAllByID(order.orderNo);

        orderService.getCompleteOrderList(orderNo);

        // 1. 현재 로그인된 회원 ID 가져오기
        //String memberId = memberDetails.getUsername();
        return "product/complete";
    }

    @GetMapping("/product/search")
    public String searchPage() {
        return "product/search";
    }

}
