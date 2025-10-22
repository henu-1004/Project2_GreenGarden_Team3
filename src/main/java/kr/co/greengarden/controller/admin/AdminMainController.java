package kr.co.greengarden.controller.admin;

import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.dto.admin.AdminIndexChartDTO;
import kr.co.greengarden.dto.admin.AdminIndexOrderInfoDTO;
import kr.co.greengarden.dto.admin.AdminIndexOrderInfoWrapperDTO;
import kr.co.greengarden.dto.admin.DeliveryDTO;
import kr.co.greengarden.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMainController {

    private final MemberService memberService;
    private final DeliveryService deliveryService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final NoticeService noticeService;
    private final InquiryService inquiryService;
    private final ChartService chartService;

    @GetMapping("/admin/")
    public String adminMainPage(PageRequestDTO pageRequestDTO,
                                @RequestParam(defaultValue = "0") int weekOffset,  // ✨ 주차 오프셋 추가
                                Model model) {

        AdminIndexOrderInfoWrapperDTO orderInfo = orderService.getAdminIndexOrderInfo();
        AdminIndexOrderInfoWrapperDTO orderInfoToday = orderService.getAdminIndexOrderInfoToday();
        AdminIndexOrderInfoWrapperDTO orderInfoYesterday = orderService.getAdminIndexOrderInfoYesterday();

        // Order테이블에서 Status("결제 대기"의 수) 정보 가져오기
        int statusCount = orderInfo.getStatusCount();

        // 주문 금액 (Order TotalPrice의 합)
        int totalPrice = orderInfo.getTotalPrice();

        // 취소, 교환, 반품
        int cancleCount = orderItemService.getCancleCount();
        int exchangeCount = orderItemService.getExchangeCount();
        int returnCount = orderItemService.getReturnCount();

        // Delivery테이블에서 Status(상태) 정보 가져오기
        int deliveryStatusCount = deliveryService.getStatusCount();

        // 주문 건 수 (Order Count)
        int orderCount = orderInfo.getCount();

        // 회원 가입 (회원 수)
        int memberCount = memberService.getMemberCount() - 1;

        // 방문자 수.. (로직 만들어야 함)


        // 문의 게시물 (문의 사항 게시물 개수)
        int qnaCount = inquiryService.getInquiryCount();

        // 공지사항 및 문의사항 게시판
        PageResponseDTO<NoticeDTO> pageResponseDTO = noticeService.getNoticesList(pageRequestDTO);
        PageResponseDTO<InquiryDTO> responseDTO = inquiryService.getInquiryList(pageRequestDTO);

        // admin chart 데이터 (주차 오프셋 전달) // 25/10/19 이수연
        AdminIndexChartDTO chartData = chartService.getAdminIndexChartData(weekOffset);

        // 오늘
        // 주문 건 수 (Order Count)
        int orderTodayCount = orderInfoToday.getCount();
        // 주문 금액 (Order TotalPrice의 합)
        int totalTodayPrice = orderInfoToday.getTotalPrice();
        // 회원 수
        int memberTodayCount = memberService.getMemberTodayCount() -1;
        if(memberTodayCount == -1) {
            memberTodayCount = 0;
        }
        // 어제
        // 주문 건 수 (Order Count)
        int orderYesterdayCount = orderInfoYesterday.getCount();
        // 주문 금액 (Order TotalPrice의 합)
        int totalYesterdayPrice = orderInfoYesterday.getTotalPrice();
        // 회원 수
        int memberYesterdayCount = memberService.getMemberYesterdayCount() -1;
        if(memberYesterdayCount == -1) {
            memberYesterdayCount = 0;
        }

        model.addAttribute("chartData", chartData);
        model.addAttribute("weekOffset", weekOffset);  // 현재 주차 정보 // admin chart.여기까지
        model.addAttribute("deliveryStatusCount", deliveryStatusCount);
        model.addAttribute("statusCount", statusCount);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("memberCount", memberCount);
        model.addAttribute("qnaCount", qnaCount);
        model.addAttribute("responseDTO",responseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("cancleCount", cancleCount);
        model.addAttribute("exchangeCount", exchangeCount);
        model.addAttribute("returnCount", returnCount);

        model.addAttribute("orderTodayCount", orderTodayCount);
        model.addAttribute("totalTodayPrice", totalTodayPrice);
        model.addAttribute("orderYesterdayCount", orderYesterdayCount);
        model.addAttribute("totalYesterdayPrice", totalYesterdayPrice);
        model.addAttribute("memberTodayCount", memberTodayCount);
        model.addAttribute("memberYesterdayCount", memberYesterdayCount);

        return "admin/index";
    }

}
