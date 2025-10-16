package kr.co.greengarden.controller.admin;

import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.service.InquiryService;
import kr.co.greengarden.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminMainController {

    private final NoticeService noticeService;
    private final InquiryService inquiryService;

    @GetMapping("/admin/")
    public String adminMainPage(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<NoticeDTO> pageResponseDTO = noticeService.getNoticesList(pageRequestDTO);
        PageResponseDTO<InquiryDTO> responseDTO = inquiryService.getInquiryList(pageRequestDTO);

        model.addAttribute("responseDTO",responseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/index";
    }

}
