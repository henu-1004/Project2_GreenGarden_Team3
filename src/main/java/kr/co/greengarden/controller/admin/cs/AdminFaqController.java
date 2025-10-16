package kr.co.greengarden.controller.admin.cs;

import kr.co.greengarden.dto.FaqDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminFaqController {

    private final FaqService faqService;

    // FAQ 목록 페이지
    @GetMapping("/admin/cs/faq/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("Faq 목록 페이지 요청 : {} ", pageRequestDTO);

        // 1. Service 호출 → 전체 목록 조회
        PageResponseDTO<FaqDTO> responseDTO = faqService.getFaqList(pageRequestDTO);
        List<FaqDTO> faqList = responseDTO.getDtoList();

        // 2. 2차 분류별로 Map으로 그룹화 (최대 10개)
        Map<String, List<FaqDTO>> faqMap = faqList.stream()
                .collect(Collectors.groupingBy(
                        FaqDTO::getCategory2,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream().limit(10).toList()
                        )
                ));

        // 3. Thymeleaf에 전달
        model.addAttribute("faqMap", faqMap);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "admin/cs/faq/list";
    }

    // FAQ 상세보기
    @GetMapping("/admin/cs/faq/view")
    public String view(Integer faqId, Model model) {
        FaqDTO faqDTO = faqService.getFaq(faqId);
        if (faqDTO == null) {
            return "redirect:/admin/cs/faq/list";
        }
        model.addAttribute("faqDTO", faqDTO);
        return "admin/cs/faq/view";
    }

    // FAQ 수정 폼
    @GetMapping("/admin/cs/faq/modify")
    public String modifyForm(int faqId, Model model) {
        FaqDTO faqDTO = faqService.getFaqById(faqId);
        if (faqDTO == null) {
            return "redirect:/admin/cs/faq/list";
        }
        model.addAttribute("faqDTO", faqDTO);
        return "admin/cs/faq/modify";
    }

    // FAQ 수정 처리
    @PostMapping("/admin/cs/faq/modify")
    public String modifyProc(FaqDTO faqDTO) {
        faqService.modifyFaq(faqDTO);
        return "redirect:/admin/cs/faq/list";
    }

    // FAQ 작성 폼
    @GetMapping("/admin/cs/faq/write")
    public String writeForm(Model model) {
        model.addAttribute("faqDTO", new FaqDTO());
        return "admin/cs/faq/write";
    }

    // FAQ 작성 처리
    @PostMapping("/admin/cs/faq/write")
    public String write(FaqDTO faqDTO) {
        int faqId = faqService.registerFaq(faqDTO);
        return "redirect:/admin/cs/faq/view?faqId=" + faqId;
    }

    // FAQ 삭제
    @PostMapping("/admin/cs/faq/delete")
    public String delete(int faqId) {
        faqService.delete(faqId);
        return "redirect:/admin/cs/faq/list";
    }
}
