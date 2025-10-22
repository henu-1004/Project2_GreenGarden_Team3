package kr.co.greengarden.controller.my;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.my.CouponSummaryDTO;
import kr.co.greengarden.dto.my.CouponTabPageDTO;
import kr.co.greengarden.dto.my.MyHomeSummaryDTO;
import kr.co.greengarden.dto.my.MyInfoDTO;
import kr.co.greengarden.dto.my.MyInfoForm;
import kr.co.greengarden.dto.my.MyInfoUpdateDTO;
import kr.co.greengarden.dto.my.MyInquiryDTO;
import kr.co.greengarden.dto.my.MyInquirySummaryDTO;
import kr.co.greengarden.dto.my.OrderDetailDTO;
import kr.co.greengarden.dto.my.OrderHistoryCriteria;
import kr.co.greengarden.dto.my.OrderHistoryPageDTO;
import kr.co.greengarden.dto.my.OrderSummaryDTO;
import kr.co.greengarden.dto.my.PagedResult;
import kr.co.greengarden.dto.my.PaginationDTO;
import kr.co.greengarden.dto.my.PointLedgerCriteria;
import kr.co.greengarden.dto.my.PointLedgerDTO;
import kr.co.greengarden.dto.my.PointLedgerPageDTO;
import kr.co.greengarden.dto.my.PointSummaryDTO;
import kr.co.greengarden.dto.my.ProductReviewDTO;
import kr.co.greengarden.dto.my.ReviewSummaryDTO;
import kr.co.greengarden.dto.my.SellerInfoDTO;
import kr.co.greengarden.service.InquiryService;
import kr.co.greengarden.service.MyCouponService;
import kr.co.greengarden.service.MyService;
import kr.co.greengarden.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;
    private final PointService pointService;
    private final MyCouponService myCouponService;
    private final InquiryService inquiryService;

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model,
                       @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("currentUri", request.getRequestURI());

        if (userDetails == null) {
            log.debug("❌ 로그인 정보 없음 (userDetails == null)");
            model.addAttribute("recentOrders", List.of());
            model.addAttribute("recentPoints", List.of());
            model.addAttribute("myReviews", List.of());
            model.addAttribute("recentInquiries", List.of());
            model.addAttribute("homeSummary", MyHomeSummaryDTO.empty());
            model.addAttribute("couponSummary", CouponSummaryDTO.builder().build());
            model.addAttribute("totalPoint", 0);
            model.addAttribute("myInfo", null);
            model.addAttribute("loginStatus", false);
        } else {
            String memId = userDetails.getUsername();
            log.debug("✅ 로그인된 사용자 ID: {}", memId);

            List<OrderSummaryDTO> recentOrders = myService.getRecentOrderSummary(memId);
            List<ProductReviewDTO> myReviews = myService.getMyReviews(memId).stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<PointLedgerDTO> recentPoints = pointService.getRecentLedger(memId);
            List<MyInquiryDTO> recentInquiries = myService.getMyInquiries(memId).stream()
                    .limit(5)
                    .collect(Collectors.toList());
            MyInfoDTO myInfo = myService.getMyInfo(memId);
            CouponSummaryDTO couponSummary = myCouponService.getCouponSummary(memId);
            int totalPoint = pointService.getTotalPoint(memId);

            long orderCount = myService.countMyOrders(memId);
            long inquiryCount = myService.countMyInquiries(memId);
            int availableCouponCount = couponSummary != null ? couponSummary.getAvailableCount() : 0;

            MyHomeSummaryDTO summary = MyHomeSummaryDTO.builder()
                    .orderCount(orderCount)
                    .availableCouponCount(availableCouponCount)
                    .totalPoint(totalPoint)
                    .inquiryCount(inquiryCount)
                    .build();

            model.addAttribute("recentOrders", recentOrders);
            model.addAttribute("myReviews", myReviews);
            model.addAttribute("recentPoints", recentPoints);
            model.addAttribute("recentInquiries", recentInquiries);
            model.addAttribute("myInfo", myInfo);
            model.addAttribute("couponSummary", couponSummary != null ? couponSummary : CouponSummaryDTO.builder().build());
            model.addAttribute("homeSummary", summary);
            model.addAttribute("totalPoint", totalPoint);
            model.addAttribute("loginStatus", true);
        }

        return "my/home";
    }

    @GetMapping("/home/order/{orderNo}")
    @ResponseBody
    public ResponseEntity<?> orderDetail(@PathVariable String orderNo,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        String memId = userDetails.getUsername();
        log.info("📦 [ORDER DETAIL] memId={}, orderNo={}", memId, orderNo);

        Optional<OrderDetailDTO> detail = myService.getOrderDetail(memId, orderNo);
        log.info("📦 결과: {}", detail.isPresent() ? "✅ 데이터 있음" : "❌ 데이터 없음");
        return detail.map(d -> ResponseEntity.ok((Object)d))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("주문 정보를 찾을 수 없습니다."));
    }


    @GetMapping("/home/seller/{sellerId}")
    @ResponseBody
    public ResponseEntity<?> sellerInfo(@PathVariable String sellerId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        log.info("🛍 [SELLER INFO] sellerId={}", sellerId);
        Optional<SellerInfoDTO> sellerInfo = myService.getSellerInfo(sellerId);
        log.info("🛍 결과: {}", sellerInfo.isPresent() ? "✅ 데이터 있음" : "❌ 데이터 없음");
        return sellerInfo.map(s -> ResponseEntity.ok((Object)s))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("판매자 정보를 찾을 수 없습니다."));
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
        model.addAttribute("myInfo", myService.getMyInfo(memId));
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
    public String point(HttpServletRequest request,
                        Model model,
                        @AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(value = "period", required = false) String period,
                        @RequestParam(value = "startDate", required = false)
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @RequestParam(value = "endDate", required = false)
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                        @RequestParam(value = "type", defaultValue = "ALL") String type,
                        @RequestParam(value = "page", defaultValue = "1") int page) {

        model.addAttribute("currentUri", request.getRequestURI());
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        String memId = userDetails.getUsername();
        LocalDate today = LocalDate.now();
        LocalDate resolvedEnd = endDate != null ? endDate : today;
        if (resolvedEnd.isAfter(today)) {
            resolvedEnd = today;
        }

        LocalDate resolvedStart;
        String selectedPeriod;
        if (period != null && !period.isBlank()) {
            switch (period) {
                case "1m":
                    resolvedStart = resolvedEnd.minusMonths(1).plusDays(1);
                    break;
                case "6m":
                    resolvedStart = resolvedEnd.minusMonths(6).plusDays(1);
                    break;
                case "12m":
                    resolvedStart = resolvedEnd.minusMonths(12).plusDays(1);
                    break;
                case "3m":
                default:
                    resolvedStart = resolvedEnd.minusMonths(3).plusDays(1);
                    period = "3m";
                    break;
            }
            selectedPeriod = period;
        } else if (startDate != null && endDate != null) {
            resolvedStart = startDate;
            selectedPeriod = "custom";
        } else {
            resolvedStart = resolvedEnd.minusMonths(3).plusDays(1);
            selectedPeriod = "3m";
        }

        if (resolvedStart.isAfter(resolvedEnd)) {
            resolvedStart = resolvedEnd;
        }

        LocalDate minStart = resolvedEnd.minusMonths(12);
        boolean limited = false;
        if (resolvedStart.isBefore(minStart)) {
            resolvedStart = minStart;
            limited = true;
        }

        String normalizedType = switch (type == null ? "ALL" : type.toUpperCase()) {
            case "EARN", "USE" -> type.toUpperCase();
            default -> "ALL";
        };

        PointLedgerCriteria criteria = PointLedgerCriteria.builder()
                .memId(memId)
                .startDate(resolvedStart)
                .endDate(resolvedEnd)
                .type(normalizedType)
                .page(page)
                .size(10)
                .build();

        PointLedgerPageDTO ledgerPage = pointService.getPointLedgerPage(criteria);
        PointSummaryDTO summary = pointService.getPointSummary(memId, resolvedStart, resolvedEnd);

        model.addAttribute("pointLogs", ledgerPage.getItems());
        model.addAttribute("pointPage", ledgerPage);
        model.addAttribute("summary", summary);
        model.addAttribute("totalPoint", summary.getTotalPoint());
        model.addAttribute("selectedPeriod", selectedPeriod);
        model.addAttribute("periodParam", "custom".equals(selectedPeriod) ? null : selectedPeriod);
        model.addAttribute("startDate", resolvedStart);
        model.addAttribute("endDate", resolvedEnd);
        model.addAttribute("limited", limited);
        model.addAttribute("selectedType", normalizedType);
        model.addAttribute("typeParam", normalizedType);
        model.addAttribute("totalCount", ledgerPage.getTotalCount());

        return "my/point";
    }

    @GetMapping("/coupon")
    public String coupon(HttpServletRequest request,
                         Model model,
                         @AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam(value = "tab", defaultValue = "available") String tab,
                         @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("currentUri", request.getRequestURI());
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        String memId = userDetails.getUsername();
        CouponTabPageDTO couponPage = myCouponService.getCouponTabPage(memId, tab, page, 6);

        model.addAttribute("couponSummary", couponPage.getSummary());
        model.addAttribute("coupons", couponPage.getPage().getItems());
        model.addAttribute("couponPage", couponPage.getPage().getPagination());
        model.addAttribute("activeTab", couponPage.getActiveTab());

        return "my/coupon";
    }

    @GetMapping("/review")
    public String review(HttpServletRequest request,
                         Model model,
                         @AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("currentUri", request.getRequestURI());
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        String memId = userDetails.getUsername();
        List<ProductReviewDTO> allReviews = myService.getMyReviews(memId);
        if (allReviews == null) {
            allReviews = List.of();
        }

        ReviewSummaryDTO summary = myService.buildReviewSummary(allReviews);
        PagedResult<ProductReviewDTO> reviewPage = myService.getMyReviewsPage(memId, page, 5);

        model.addAttribute("reviewList", reviewPage.getItems());
        model.addAttribute("reviewSummary", summary);
        model.addAttribute("reviewPage", reviewPage.getPagination());

        return "my/review";
    }

    @GetMapping("/qna")
    public String qna(HttpServletRequest request,
                      Model model,
                      @AuthenticationPrincipal UserDetails userDetails,
                      @RequestParam(value = "page", defaultValue = "1") int page) {
        model.addAttribute("currentUri", request.getRequestURI());
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        String memId = userDetails.getUsername();
        List<MyInquiryDTO> allInquiries = myService.getMyInquiries(memId);
        if (allInquiries == null) {
            allInquiries = List.of();
        }

        MyInquirySummaryDTO summary = myService.buildInquirySummary(allInquiries);
        PagedResult<MyInquiryDTO> qnaPage = myService.getMyInquiryPage(memId, page, 10);

        model.addAttribute("qnaList", qnaPage.getItems());
        model.addAttribute("qnaSummary", summary);
        model.addAttribute("qnaPage", qnaPage.getPagination());

        return "my/qna";
    }

    @GetMapping("/info")
    public String info(HttpServletRequest request,
                       Model model,
                       @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("currentUri", request.getRequestURI());
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        String memId = userDetails.getUsername();
        MyInfoDTO info = myService.getMyInfo(memId);
        if (info == null) {
            info = new MyInfoDTO();
            info.setMemId(memId);
        }

        int totalPoint = pointService.getTotalPoint(memId);
        info.setTotalPoint(totalPoint);
        CouponSummaryDTO couponSummary = myCouponService.getCouponSummary(memId);
        info.setAvailableCouponCount(couponSummary.getAvailableCount());

        String maskedMemId = maskMemberId(info.getMemId() != null ? info.getMemId() : memId);

        model.addAttribute("info", info);
        model.addAttribute("couponSummary", couponSummary);
        model.addAttribute("infoForm", MyInfoForm.from(info));
        model.addAttribute("maskedMemId", maskedMemId);

        return "my/info";
    }

    @PostMapping("/info")
    public String updateInfo(@AuthenticationPrincipal UserDetails userDetails,
                             @ModelAttribute("infoForm") MyInfoForm form,
                             RedirectAttributes redirect) {
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        if (form.getName() == null || form.getName().isBlank()) {
            redirect.addFlashAttribute("infoErrorMessage", "이름을 입력해주세요.");
            return "redirect:/my/info";
        }

        if (form.getBirth() != null && form.getBirth().isAfter(LocalDate.now())) {
            redirect.addFlashAttribute("infoErrorMessage", "생년월일은 오늘 이후로 설정할 수 없습니다.");
            return "redirect:/my/info";
        }

        if (form.getCurrentPassword() == null || form.getCurrentPassword().isBlank()) {
            redirect.addFlashAttribute("infoErrorMessage", "현재 비밀번호를 입력해주세요.");
            return "redirect:/my/info";
        }

        boolean wantsPasswordChange = form.getNewPassword() != null && !form.getNewPassword().isBlank();
        if (wantsPasswordChange) {
            if (form.getConfirmPassword() == null || form.getConfirmPassword().isBlank()) {
                redirect.addFlashAttribute("infoErrorMessage", "새 비밀번호 확인을 입력해주세요.");
                return "redirect:/my/info";
            }
            if (!form.getNewPassword().equals(form.getConfirmPassword())) {
                redirect.addFlashAttribute("infoErrorMessage", "새 비밀번호가 일치하지 않습니다.");
                return "redirect:/my/info";
            }
        }

        String memId = userDetails.getUsername();
        if (!myService.verifyPassword(memId, form.getCurrentPassword())) {
            redirect.addFlashAttribute("infoErrorMessage", "비밀번호가 틀렸습니다.");
            return "redirect:/my/info";
        }

        MyInfoUpdateDTO updateDTO = MyInfoUpdateDTO.builder()
                .memId(memId)
                .name(form.getName())
                .birth(form.getBirth())
                .gender(form.getGender())
                .email(form.getEmail())
                .phone(form.getPhone())
                .zipCode(form.getZipCode())
                .addressBasic(form.getAddressBasic())
                .addressDetail(form.getAddressDetail())
                .newPassword(wantsPasswordChange ? form.getNewPassword() : null)
                .build();

        try {
            myService.updateMyInfo(updateDTO);
            redirect.addFlashAttribute("infoSuccessMessage", "회원 정보가 업데이트되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirect.addFlashAttribute("infoErrorMessage", ex.getMessage());
        }

        return "redirect:/my/info";
    }

    @PostMapping("/info/withdraw")
    public String withdraw(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam("password") String password,
                           RedirectAttributes redirect) {
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        if (password == null || password.isBlank()) {
            redirect.addFlashAttribute("withdrawErrorMessage", "비밀번호를 입력해주세요.");
            return "redirect:/my/info";
        }

        String memId = userDetails.getUsername();
        if (!myService.verifyPassword(memId, password)) {
            redirect.addFlashAttribute("withdrawErrorMessage", "비밀번호가 틀렸습니다.");
            return "redirect:/my/info";
        }

        try {
            myService.withdrawMember(memId);
            redirect.addFlashAttribute("withdrawSuccessMessage", "회원 탈퇴 처리가 완료되었습니다. 고객센터를 통해 재가입을 도와드릴 수 있습니다.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            log.error("❌ 회원 탈퇴 처리 실패", ex);
            redirect.addFlashAttribute("withdrawErrorMessage", ex.getMessage());
        } catch (Exception ex) {
            log.error("❌ 회원 탈퇴 처리 중 알 수 없는 오류", ex);
            redirect.addFlashAttribute("withdrawErrorMessage", "회원 탈퇴 처리 중 오류가 발생했습니다.");
        }

        return "redirect:/my/info";
    }

    private String maskMemberId(String memId) {
        if (memId == null || memId.isBlank()) {
            return "-";
        }
        int visible = Math.min(3, memId.length());
        String prefix = memId.substring(0, visible);
        String masked = "*".repeat(memId.length() - visible);
        return prefix + masked;
    }

    @PostMapping("/inquiry")
    public String submitInquiry(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("category1") String category1,
                                @RequestParam(value = "category2", required = false) String category2,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value = "redirect", required = false) String redirectTarget,
                                RedirectAttributes redirect) {
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        String trimmedTitle = title == null ? null : title.trim();
        String trimmedContent = content == null ? null : content.trim();

        if (trimmedTitle == null || trimmedTitle.isBlank() ||
                trimmedContent == null || trimmedContent.isBlank()) {
            redirect.addFlashAttribute("errorMsg", "제목과 내용을 모두 입력해주세요.");
            return resolveRedirect(redirectTarget);
        }

        String primary = (category1 == null || category1.isBlank()) ? "기타" : category1.trim();
        String secondary = (category2 == null || category2.isBlank()) ? "MY_PAGE" : category2.trim();

        try {
            InquiryDTO inquiryDTO = InquiryDTO.builder()
                    .category1(primary)
                    .category2(secondary)
                    .title(trimmedTitle)
                    .content(trimmedContent)
                    .channel("MY_PAGE")
                    .build();
            inquiryService.registerInquiry(inquiryDTO);
            redirect.addFlashAttribute("msg", "문의가 등록되었습니다.");
        } catch (Exception ex) {
            log.error("❌ 문의 등록 실패", ex);
            redirect.addFlashAttribute("errorMsg", "문의 등록 중 오류가 발생했습니다.");
        }

        return resolveRedirect(redirectTarget);
    }


    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam String orderNo,
                               @RequestParam Long orderItemId,
                               @RequestParam(value = "redirect", required = false) String redirectTarget,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirect) {
        myService.updateConfirmYn(orderNo, orderItemId, "Y");
        redirect.addFlashAttribute("msg", "구매확정 완료되었습니다.");
        return resolveRedirect(redirectTarget);
    }

    @PostMapping("/review/complete")
    public String completeReview(@ModelAttribute kr.co.greengarden.dto.my.ProductReviewDTO reviewDTO,
                                 @RequestParam(value = "redirect", required = false) String redirectTarget,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirect) {

        if (userDetails == null) {
            redirect.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        reviewDTO.setMemId(userDetails.getUsername());
        myService.writeProductReview(reviewDTO);

        redirect.addFlashAttribute("msg", "상품평이 등록되었습니다!");
        return resolveRedirect(redirectTarget);
    }

    @PostMapping("/exchange/complete")
    public String completeExchange(@RequestParam String orderNo,
                                   @RequestParam Long orderItemId,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "detail", required = false) String detail,
                                   @RequestParam(value = "proof", required = false) MultipartFile proof,
                                   @RequestParam(value = "redirect", required = false) String redirectTarget,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirect) {
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        try {
            myService.submitExchange(userDetails.getUsername(), orderNo, orderItemId, type, detail, proof);
            redirect.addFlashAttribute("msg", "교환 신청이 접수되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirect.addFlashAttribute("errorMsg", ex.getMessage());
        }

        return resolveRedirect(redirectTarget);
    }

    @PostMapping("/return/complete")
    public String completeReturn(@RequestParam String orderNo,
                                 @RequestParam Long orderItemId,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "detail", required = false) String detail,
                                 @RequestParam(value = "proof", required = false) MultipartFile proof,
                                 @RequestParam(value = "redirect", required = false) String redirectTarget,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirect) {
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        try {
            myService.submitReturnRequest(userDetails.getUsername(), orderNo, orderItemId, type, detail, proof);
            redirect.addFlashAttribute("msg", "반품 신청이 접수되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirect.addFlashAttribute("errorMsg", ex.getMessage());
        }

        return resolveRedirect(redirectTarget);
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderNo,
                              @RequestParam Long orderItemId,
                              @RequestParam(value = "redirect", required = false) String redirectTarget,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirect) {
        if (userDetails == null) {
            return "redirect:/member/login";
        }

        try {
            myService.cancelOrderItem(userDetails.getUsername(), orderNo, orderItemId);
            redirect.addFlashAttribute("msg", "주문이 취소되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirect.addFlashAttribute("errorMsg", ex.getMessage());
        }

        return resolveRedirect(redirectTarget);
    }



    private String resolveRedirect(String target) {
        if (target != null && target.equalsIgnoreCase("order")) {
            return "redirect:/my/order";
        }
        return "redirect:/my/home";
    }

}
