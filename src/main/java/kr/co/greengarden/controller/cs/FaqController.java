package kr.co.greengarden.controller.cs;


import kr.co.greengarden.dto.FaqDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * 날짜 : 2025/09/26 , 2025/10/13
 * 이름 : 박효빈
 * 내용 : FaqController  추가 + FaqController List View 구현
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // 목록 페이지 출력
    @GetMapping("/cs/faq/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("Faq 목록 페이지 요청 : {} ", pageRequestDTO);

        // 1. Service를 호출하여 필터링 조건에 맞는 전체 목록 조회하기
        PageResponseDTO<FaqDTO> responseDTO = faqService.getFaqList(pageRequestDTO);

        // 2. 조회된 전체 FaqDTO 리스트 추출
        List<FaqDTO> faqList = responseDTO.getDtoList();

        // 3. 💡 핵심: List를 'CATEGORY2' (2차 분류) 기준으로 Map으로 그룹화
        // 이 Map은 뷰에서 '가입', '탈퇴' 등의 제목과 해당 목록을 반복 출력하는 데 사용
        // Collectors.groupingBy(FaqDTO::getCategory2) : FaqDTO 객체들을 getCategory2() 값이 같은 것끼리 묶어 Map으로 만듭니다.
        Map<String, List<FaqDTO>> faqMap = faqList.stream()
                .collect(Collectors.groupingBy(FaqDTO::getCategory2));

        // 이후 Timeleaf 에서 ${pageResponseDTO} << 로 접근
        model.addAttribute("faqMap",faqMap);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "cs/faq/list";
    }

    @GetMapping("/cs/faq/view")
    public String view(Integer faqId, Model model) {
        // 요청 로그 확인
        log.info("FAQ 상세 보기 요청 : {}", faqId);

        // 1. 서비스 호출
        faqService.getFaq(faqId);
        log.info("FAQ 조회수 1 증가 : {}", faqId);

        // 2. 상세 정보 조회
        FaqDTO faqDTO = faqService.getFaq(faqId);

        if (faqDTO == null) {
            return "redirect:/cs/faq/list";
        }

        // 조회된 데이터를 model에 담아 View로 전달
        model.addAttribute("faqDTO", faqDTO);
        return "cs/faq/view";
    }
}
