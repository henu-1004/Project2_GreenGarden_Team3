package kr.co.greengarden.controller.admin;

import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.dto.admin.AdminIndexOrderInfoDTO;
import kr.co.greengarden.dto.admin.AdminIndexOrderInfoWrapperDTO;
import kr.co.greengarden.service.InquiryService;
import kr.co.greengarden.service.MemberService;
import kr.co.greengarden.service.NoticeService;
import kr.co.greengarden.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMainController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final NoticeService noticeService;
    private final InquiryService inquiryService;

    @GetMapping("/admin/")
    public String adminMainPage(PageRequestDTO pageRequestDTO, Model model) {

        AdminIndexOrderInfoWrapperDTO orderInfo = orderService.getAdminIndexOrderInfo();

        // Order테이블에서 Status("결제 대기"의 수) 정보 가져오기
        int statusCount = orderInfo.getStatusCount();

        // 주문 금액 (Order TotalPrice의 합)
        int totalPrice = orderInfo.getTotalPrice();

        // Delivery테이블에서 Status(상태) 정보 가져오기

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


        model.addAttribute("statusCount", statusCount);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("memberCount", memberCount);
        model.addAttribute("qnaCount", qnaCount);
        model.addAttribute("responseDTO",responseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/index";
    }

}
