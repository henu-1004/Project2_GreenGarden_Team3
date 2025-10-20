package kr.co.greengarden.controller.admin.cs;


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

/*
 * 날짜 : 2025/09/26
 * 이름 : 박효빈
 * 내용 : InquiryController  추가
 */

/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/13
 * 내용 : 고객센터 - AdminQnaController 구현
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminQnaController {
    private final InquiryService inquiryService;

    // 전체 리스트
    @GetMapping("/admin/cs/qna/list")
    public String list(Model model, PageRequestDTO pageRequestDTO) {
        log.info("qna List Request", pageRequestDTO.toString());

        // 1. service 호출하여 리스트 데이터와 페이징 정보 가져오기
        PageResponseDTO<InquiryDTO> responseDTO = inquiryService.getInquiryList(pageRequestDTO);

        // 2. 모델에 담은 뒤 View 전달
        model.addAttribute("responseDTO",responseDTO);
        // 검색, 페이징 조건 유지 하기위해 requestDTO전달
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "admin/cs/qna/list";
    }

    // 글보기
    @GetMapping("/admin/cs/qna/view")
    // int -> Integer (래퍼 클래스)로 변경하여 null 값을 처리할 수 있게 함
    public String view(Integer inquiryId,PageRequestDTO pageRequestDTO ,Model model) {

        // inquiryId가 null 이거나 0 이하일 경우 목록으로 리다이렉트하여 오류 방지
        if (inquiryId == null || inquiryId <= 0) {
            return "redirect:/admin/cs/qna/list";
        }

        InquiryDTO inquiryDTO = inquiryService.getInquiry(inquiryId);
        if (inquiryDTO == null) {
            return "redirect:/admin/cs/qna/list";
        }
        model.addAttribute("inquiryDTO",inquiryDTO);
        model.addAttribute("PageRequestDTO",pageRequestDTO);
        return "admin/cs/qna/view";
    }

    @GetMapping("/admin/cs/qna/reply")
    public String reply(Integer inquiryId,PageRequestDTO pageRequestDTO,Model model) {
        InquiryDTO inquiryDTO = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiryDTO",inquiryDTO);
        return "admin/cs/qna/reply";
    }
    // 답변 등록 처리 POST
    @PostMapping("/admin/cs/qna/reply")
    public String saverReply(Integer inquiryId,String answer,RedirectAttributes redirectAttributes) {

        inquiryService.saveAnswer(inquiryId,answer);
        return "redirect:/admin/cs/qna/list";
    }

    // 문의 작성폼
    @GetMapping("/admin/cs/qna/write")
    public String writeForm() {
        return "admin/cs/qna/write";
    }

    // 문의 작성 처리 (서버) Post
    @PostMapping("/admin/cs/qna/write")
    public String writeProcess(InquiryDTO inquiryDTO, RedirectAttributes rttr){

        // 1. 문의 저장 및 새로 생성된 id 획득
        int newId = inquiryService.registerInquiry(inquiryDTO);
        log.info("Inquiry register inquiry ID:{}", newId);

        // 2. 리다이렉트 후 사용자에게 보여줄 메세지설정 (일회용(
        rttr.addFlashAttribute("result","success");
        rttr.addFlashAttribute("msg","문의가 성공적으로 등록되었습니다");

        // 3. 상세 페이지로 리다렉트
        return "redirect:/admin/cs/qna/view?inquiryId="+newId;
    }


    @PostMapping("/admin/cs/qna/delete")
    public String adminCsDelete(int inquiryId) {
        inquiryService.delete(inquiryId);
        return "redirect:/admin/cs/qna/list";
    }

    // 선택삭제
    @PostMapping("/admin/cs/qna/deleteMultiple")
    public String deleteMultiple(String qnaIds) {
        log.info("qnaIds:{}", qnaIds);
        List<Integer> ids = Arrays.stream(qnaIds.split(","))
                .map(Integer::parseInt)
                .toList();

        inquiryService.deleteMultiple(ids);
        return "redirect:/admin/cs/qna/list";
    }
}
