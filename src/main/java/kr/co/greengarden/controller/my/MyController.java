package kr.co.greengarden.controller.my;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.greengarden.dto.my.OrderHistoryCriteria;
import kr.co.greengarden.dto.my.OrderHistoryPageDTO;
import kr.co.greengarden.service.MyService;
import kr.co.greengarden.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;
    private final PointService pointService;

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

            // ✅ 내가 작성한 상품평 내역 조회
            model.addAttribute("myReviews", myService.getMyReviews(memId));

            // ✅ 포인트 내역 (최근 5건 + 총 포인트)
            model.addAttribute("recentPoints", pointService.getRecentLedger(memId));
            model.addAttribute("totalPoint", pointService.getTotalPoint(memId));

            model.addAttribute("loginStatus", true);
        }

        return "my/home";
    }


    @GetMapping("/order")
    public String order(HttpServletRequest request,
                       Model model,
                       @AuthenticationPrincipal UserDetails userDetails,
                       @RequestParam(value = "period", required = false) String period,
                       @RequestParam(value = "startDate", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                       @RequestParam(value = "endDate", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                       @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("currentUri", request.getRequestURI());
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        String memId = userDetails.getUsername();

        LocalDate today = LocalDate.now();
        LocalDate resolvedEnd = (endDate != null) ? endDate : today;
        if (resolvedEnd.isAfter(today)) {
            resolvedEnd = today;
        }

        LocalDate resolvedStart;
        String selectedPeriod;
        if (period != null && !period.isBlank()) {
            switch (period) {
                case "1w":
                    resolvedStart = resolvedEnd.minusDays(6);
                    break;
                case "15d":
                    resolvedStart = resolvedEnd.minusDays(14);
                    break;
                case "1m":
                default:
                    resolvedStart = resolvedEnd.minusMonths(1).plusDays(1);
                    period = "1m";
                    break;
            }
            selectedPeriod = period;
        } else if (startDate != null && endDate != null) {
            resolvedStart = startDate;
            selectedPeriod = "custom";
        } else {
            resolvedStart = resolvedEnd.minusMonths(1).plusDays(1);
            selectedPeriod = "1m";
        }

        if (resolvedStart.isAfter(resolvedEnd)) {
            resolvedStart = resolvedEnd;
        }

        LocalDate minStart = resolvedEnd.minusMonths(5);
        boolean limited = false;
        if (resolvedStart.isBefore(minStart)) {
            resolvedStart = minStart;
            limited = true;
        }

        OrderHistoryCriteria criteria = OrderHistoryCriteria.builder()
                .memId(memId)
                .startDate(resolvedStart)
                .endDate(resolvedEnd)
                .startDateTime(resolvedStart.atStartOfDay())
                .endDateTime(resolvedEnd.atTime(LocalTime.of(23, 59, 59)))
                .page(page)
                .size(10)
                .build();

        OrderHistoryPageDTO pageDTO = myService.getOrderHistory(criteria);

        model.addAttribute("orders", pageDTO.getOrders());
        model.addAttribute("pageInfo", pageDTO);
        model.addAttribute("startDate", resolvedStart);
        model.addAttribute("endDate", resolvedEnd);
        model.addAttribute("selectedPeriod", selectedPeriod);
        model.addAttribute("periodParam", "custom".equals(selectedPeriod) ? null : selectedPeriod);
        model.addAttribute("limited", limited);
        model.addAttribute("totalCount", pageDTO.getTotalCount());

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
                               @RequestParam Long proId,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirect) {
        myService.updateConfirmYn(orderNo, proId, "Y");
        redirect.addFlashAttribute("msg", "구매확정 완료되었습니다.");
        return "redirect:/my/home";
    }

    @PostMapping("/review/complete")
    public String completeReview(@ModelAttribute kr.co.greengarden.dto.my.ProductReviewDTO reviewDTO,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirect) {

        if (userDetails == null) {
            redirect.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        reviewDTO.setMemId(userDetails.getUsername());
        myService.writeProductReview(reviewDTO);

        redirect.addFlashAttribute("msg", "상품평이 등록되었습니다!");
        return "redirect:/my/home";
    }

    @PostMapping("/exchange/complete")
    public String completeExchange(@RequestParam String orderNo,
                                   @RequestParam Long proId,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirect) {
        myService.updateExchangeYn(orderNo, proId, "Y");
        redirect.addFlashAttribute("msg", "교환신청 완료!");
        return "redirect:/my/home";
    }

    @PostMapping("/return/complete")
    public String completeReturn(@RequestParam String orderNo,
                                 @RequestParam Long proId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirect) {
        myService.updateReturnYn(orderNo, proId, "Y");
        redirect.addFlashAttribute("msg", "반품신청 완료!");
        return "redirect:/my/home";
    }



}
