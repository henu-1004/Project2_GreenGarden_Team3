package kr.co.greengarden.controller.cs;


import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * 날짜 : 2025/09/26
 * 이름 : 박효빈
 * 내용 : InquiryController  추가
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    // 전체 리스트
    @GetMapping("/cs/inquiry/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        log.info("Inquiry List Request", pageRequestDTO.toString());

        // 1. service 호출하여 리스트 데이터와 페이징 정보 가져오기
        PageResponseDTO<InquiryDTO> responseDTO = inquiryService.getInquiryList(pageRequestDTO);

        // 2. 모델에 담은 뒤 View 전달
        model.addAttribute("responseDTO",responseDTO);
        // 검색, 페이징 조건 유지 하기위해 requestDTO전달
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "cs/inquiry/list";
    }

    // 글보기
    @GetMapping("/cs/inquiry/view")
    // int -> Integer (래퍼 클래스)로 변경하여 null 값을 처리할 수 있게 함
    public String view(Integer inquiryId, Model model) {

        // inquiryId가 null 이거나 0 이하일 경우 목록으로 리다이렉트하여 오류 방지
        if (inquiryId == null || inquiryId <= 0) {
            return "redirect:/cs/inquiry/list";
        }

        InquiryDTO inquiryDTO = inquiryService.getInquiry(inquiryId);
        if (inquiryDTO == null) {
            return "redirect:/cs/inquiry/list";
        }
        model.addAttribute("inquiryDTO",inquiryDTO);
        return "cs/inquiry/view";
    }

    // 문의 작성폼
    @GetMapping("/cs/inquiry/write")
    public String writeForm() {
        return "cs/inquiry/write";
    }

    // 문의 작성 처리 (서버) Post
    @PostMapping("/cs/inquiry/write")
    public String writeProcess(InquiryDTO inquiryDTO, RedirectAttributes rttr){

        // 1. 문의 저장 및 새로 생성된 id 획득
        int newId = inquiryService.registerInquiry(inquiryDTO);
        log.info("Inquiry register inquiry ID:{}", newId);

        // 2. 리다이렉트 후 사용자에게 보여줄 메세지설정 (일회용(
        rttr.addFlashAttribute("result","success");
        rttr.addFlashAttribute("msg","문의가 성공적으로 등록되었습니다");

        // 3. 상세 페이지로 리다렉트
        return "redirect:/cs/inquiry/view?inquiryId="+newId;
    }
}
