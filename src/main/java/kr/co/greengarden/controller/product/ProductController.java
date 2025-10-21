package kr.co.greengarden.controller.product;

/*
    날짜 : 2025/09/26 & 2025/10/2
    이름 : 이수연 & 한탁원
    내용 : 상품 컨트롤러 & 기능
*/

import kr.co.greengarden.dto.*;
import kr.co.greengarden.entity.*;
import kr.co.greengarden.repository.OrderRepository;
import kr.co.greengarden.security.MemberDetails;
import kr.co.greengarden.service.*;
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

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final MemberService memberService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;

    @GetMapping("/product/list")
    public String productListPage(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "proId") String sortBy,
                                  @RequestParam(defaultValue = "desc") String direction,
                                  @RequestParam(defaultValue = "plants") String slug,
                                  Model model) {

        Page<ProductListDTO> productList = productService.getProductCards(page, sortBy, direction, slug);

        for (ProductListDTO p : productList) {
            int original = (int) Math.ceil(p.getPrice() * (100 - p.getDiscountRate()) / 100);
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
        Product product = productService.getViewProduct(Integer.parseInt(proId));

        productService.updateViewProduct(Integer.parseInt(proId));

        model.addAttribute("product", product);

        return "product/view";
    }

    @GetMapping("/product/cart")
    public String cartPage2(@RequestParam(defaultValue = "0") int page, @AuthenticationPrincipal MemberDetails memberDetails, Model model, RedirectAttributes ra) {
        Page<CartListDTO> cartList = cartService.getCartPage(memberDetails.getUsername(), page, 5);

        model.addAttribute("cartList", cartList);

        return "product/cart";
    }

/*    @GetMapping("/product/order2")
    public String orderPage(int productId, Integer quantity, Model model) {
        Product product = productService.getProduct(productId);
        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity); // 선택한 수량 전달

        return "product/order2";
    }*/

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

    @PostMapping("/product/order")
    public String order(@RequestParam("cartIds") List<Integer> cartIds, Model model) {
        List<CartListDTO> cartList = cartService.getCartListBycartIds(cartIds);

        // 삭제, 멤버 주문 번호
        model.addAttribute("orderInfo", orderService.getOrderInfo(cartList));
        model.addAttribute("cartList", cartList);

        return "product/order";
    }

    @GetMapping("/product/order")
    public String orderPage(@RequestParam(required = false) String cartId,
                            @RequestParam(required = false) Integer productId,
                            @RequestParam(required = false) Integer quantity,
                            Model model) {

        // 장바구니 구매 시
        if (cartId != null) {
            List<CartListDTO> cartList = cartService.getCartList(Integer.parseInt(cartId));
            model.addAttribute("orderInfo", orderService.getOrderInfo(cartList));
            model.addAttribute("cartList", cartList);

            return "product/order";  //  (경로 포함)
        }

        // 상품 상세 (view.html)에서 바로 구매 한 경우
        if (productId != null && quantity != null) {
            Product product = productService.getViewProduct(productId);
            model.addAttribute("product", product);
            model.addAttribute("quantity", quantity);

            // ✅ orderInfo 기본값 생성
            OrderInfoDTO orderInfo = new OrderInfoDTO();
            orderInfo.setCount(quantity);
            orderInfo.setOriginalTotalPrice(product.getPrice() * quantity);
            orderInfo.setDiscountPrice((int)(product.getPrice() * product.getDiscountRate() / 100.0));
            orderInfo.setDeliveryFee(product.getDeliveryFee());
            orderInfo.setTotalPrice(product.getPrice() * quantity + product.getDeliveryFee());
            orderInfo.setTotalPoint(product.getPoint() * quantity);
            model.addAttribute("orderInfo", orderInfo);

            return "product/order";  // ✅ 수정됨 (경로 포함)
        }

        return "redirect:/product/list";
    }


    @PostMapping("/product/orderFix")
    public String orderFix(OrderDTO orderDTO, OrderItemListWrapper orderItemList, Integer productId, Integer quantity, @AuthenticationPrincipal MemberDetails memberDetails) {
        // 결제 대기 -> 결제 완료

        /*  2025/10/16 한탁원
            1. prodId(전체)
            2. 배송정보 (이름 연락처 주소 우편번호 기본주소 상세주소 배송메모)
            3. 최종결제
            4. 최종결제방법
        */
        /* 2025/10/16 한탁원
           1. orderDTO를 이용해서 Order 테이블에 데이터 삽입 (Member Security에서 MemberDetail를 통해 ID 가져옴) -> orderNo 가 생김.
           2. orderNo를 통해 orderItem 테이블에 데이터 삽입
           3.
         */

        /* 2025/10/21 박효빈
         * 1. 상품 1개 상세보기 - 구매 로직 구현
         * */
        // 만약 orderItemList가 null , 이거나 items가 비었을 때 ( 상품 상세에서 바로 구매한 경우 로직)
        if (orderItemList == null || orderItemList.getItems() == null || orderItemList.getItems().isEmpty()) {
            orderItemList = new OrderItemListWrapper();
            List<OrderItemDTO> items = new ArrayList<>();

            // + form에서 prouctId, quantity 전달된 경우만 처리
            if (productId != null && quantity != null) {
                Product product = productService.getViewProduct(productId);

                OrderItemDTO item = new OrderItemDTO();
                item.setProId(productId);
                item.setQuantity(quantity);
                item.setPrice(product.getPrice());
                item.setDiscountRate(product.getDiscountRate());

                items.add(item);
            }

            orderItemList.setItems(items);
        }
        orderService.orderRegister(orderDTO, orderItemList, memberDetails);
        return "redirect:/product/complete?orderNo=" + orderDTO.getOrderNo();
    }

    /* 2025/10/17 이수연 & 2025/10/21 박효빈*/
 /*   @GetMapping("/product/complete")
    public String completePage(@AuthenticationPrincipal MemberDetails memberDetails, @RequestParam String orderNo, Model model) {
        *//*
            1. 주문완료된 정보가 그대로 보이도록 하기

            2. 주문완료된 상품은 장바구니에서 지우기
        *//*


        // - 주문 정보 가져오기 (Order 테이블) findById
        //Order order = orderRepository.findByID(orderNo);
        // - OrderNo - OrderItem 테이블 정보 가져오기
        //List<OrderItem> orderItemList = re.findAllByID(order.orderNo);

        orderService.getCompleteOrderList(orderNo);

        // 1. 현재 로그인된 회원 ID 가져오기
        //String memberId = memberDetails.getUsername();
        return "product/complete";
    }
*/

    // complete.html page 현재 (10/21)일자 기준 채울 수 있는 부분만 DB에서 가져와서 채움
    @GetMapping("/product/complete")
    public String completePage(String orderNo, Model model) {

        // 주문 번호 기반 전체 주문 정보 조회
        orderService.getCompleteOrderList(orderNo);

        // 일단 상품 한개만 예시로 product 전달 (주문정보 가져오기)
        Order order = orderRepository.findById(orderNo).orElse(null);

        // product 정보 추출 (주문 상품 중 첫 번째로 기준)
        if (order != null && order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            Product product = order.getOrderItems().get(0).getProduct();
            int quantity = order.getOrderItems().get(0).getQuantity(); // ✅ 변수 선언 추가!


            model.addAttribute("product", product);
            model.addAttribute("order", order);
            model.addAttribute("quantity", quantity); // ✅ 추가

        }
        // 주문 번호 전달
        model.addAttribute("orderNo", orderNo);

        return "product/complete";
    }
    @GetMapping("/product/search")
    public String searchPage() {
        return "product/search";
    }

}
