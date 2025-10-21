package kr.co.greengarden.controller.company;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.dto.RecruitDTO;
import kr.co.greengarden.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CompanyController {

    private final RecruitService recruitService;

    @GetMapping ("/company/index")
    public String indexPage(Model model, HttpServletRequest request) { // 1. request 객체 받기
        // 2. 현재 요청된 URI를 'currentUri'라는 이름으로 뷰에 전달
        model.addAttribute("currentUri", request.getRequestURI());
        return "company/index";
    }

    @GetMapping("/company/culture")
    public String culturePage(Model model, HttpServletRequest request) { // 1. request 객체 받기
        // 2. 현재 요청된 URI를 'currentUri'라는 이름으로 뷰에 전달
        model.addAttribute("currentUri", request.getRequestURI());
        return "company/culture";
    }

    @GetMapping("/company/story")
    public String storyPage(Model model, HttpServletRequest request) { // 1. request 객체 받기
        // 2. 현재 요청된 URI를 'currentUri'라는 이름으로 뷰에 전달
        model.addAttribute("currentUri", request.getRequestURI());
        return "company/story";
    }

    /**
     * 채용 목록 페이지 (페이징, 검색 포함)
     * URL: /admin/cs/recruit/list
     */
    @GetMapping("/company/recruit")
    public String recruitPage(PageRequestDTO pageRequestDTO,Model model, HttpServletRequest request) { // 1. request 객체 받기
        // 2. 현재 요청된 URI를 'currentUri'라는 이름으로 뷰에 전달
        model.addAttribute("currentUri", request.getRequestURI());

        PageResponseDTO<RecruitDTO> responseDTO = recruitService.getRecruitList(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);


        return "company/recruit";
    }

    @GetMapping("/company/media")
    public String mediaPage(Model model, HttpServletRequest request) { // 1. request 객체 받기
        // 2. 현재 요청된 URI를 'currentUri'라는 이름으로 뷰에 전달
        model.addAttribute("currentUri", request.getRequestURI());
        return "company/media";
    }

}
