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
import org.springframework.web.bind.annotation.RequestMapping;

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
}
