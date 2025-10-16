package kr.co.greengarden.controller.my;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.greengarden.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model,
                       @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("currentUri", request.getRequestURI());

        if (userDetails == null) {
            System.out.println("❌ 로그인 정보 없음 (userDetails == null)");
            model.addAttribute("recentOrders", List.of());
            model.addAttribute("loginStatus", false);
        } else {
            String memId = userDetails.getUsername();
            System.out.println("✅ 로그인된 사용자 ID: " + memId);

            // ✅ MyBatis 기반 최근 주문내역 조회 (상품명, 이미지 포함)
            model.addAttribute("recentOrders", myService.getRecentOrderSummary(memId));
            model.addAttribute("loginStatus", true);
        }


        return "my/home";
    }

    @GetMapping("/order")
    public String order(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/order";
    }

    @GetMapping("/point")
    public String point(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/point";
    }

    @GetMapping("/coupon")
    public String coupon(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/coupon";
    }

    @GetMapping("/review")
    public String review(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/review";
    }

    @GetMapping("/qna")
    public String qna(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/qna";
    }

    @GetMapping("/info")
    public String info(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/info";
    }


    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam String orderNo,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirect) {
        myService.updateConfirmYn(orderNo, "Y");
        redirect.addFlashAttribute("msg", "구매확정 완료되었습니다.");
        return "redirect:/my/home";
    }

    @PostMapping("/review/complete")
    public String completeReview(@RequestParam String orderNo,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirect) {
        myService.updateReviewYn(orderNo, "Y");
        redirect.addFlashAttribute("msg", "상품평 작성완료!");
        return "redirect:/my/home";
    }

    @PostMapping("/exchange/complete")
    public String completeExchange(@RequestParam String orderNo,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirect) {
        myService.updateExchangeYn(orderNo, "Y");
        redirect.addFlashAttribute("msg", "교환신청 완료!");
        return "redirect:/my/home";
    }

    @PostMapping("/return/complete")
    public String completeReturn(@RequestParam String orderNo,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirect) {
        myService.updateReturnYn(orderNo, "Y");
        redirect.addFlashAttribute("msg", "반품신청 완료!");
        return "redirect:/my/home";
    }

}
