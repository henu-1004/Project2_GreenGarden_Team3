package kr.co.greengarden.controller.company;

import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CompanyContoller {

    private final NoticeService noticeService;

    @GetMapping("/company/")
    public String indexPage() {
        return "company/index";
    }

    @GetMapping("/company/culture")
    public String culturePage() {
        return "company/culture";
    }

    @GetMapping("/company/story")
    public String storyPage() {
        return "company/story";
    }

    @GetMapping("/company/recruit")
    public String recruitPage() {
        return "company/recruit";
    }

    @GetMapping("/company/media")
    public String mediaPage() {
        return "company/media";
    }

}
