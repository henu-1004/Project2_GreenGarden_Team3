package kr.co.greengarden.controller.cs;

import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 날짜 : 2025/09/26
 * 이름 : 박효빈
 * 내용 : NoticeController 추가 연결
 */
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/notice")
@Controller
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/cs/notice/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {

        //Service 에서 데이터 조회
        PageResponseDTO<NoticeDTO> pageResponseDTO = noticeService.getNoticesList(pageRequestDTO);

        // Model에 담아서 view에 전달
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "cs/notice/list";
    }

    @GetMapping("/cs/notice/view")
    public String view(Integer noticeId, Model model) {

        // Service 데이터 조회 (조회수 증가)
        NoticeDTO noticeDTO = noticeService.getNotice(noticeId);

        if (noticeDTO == null) {
            log.info("공지사항 없음 목록으로  이동");
            return "redirect:/cs/notice/list";
        }

        //Model 에 담아서 View로 전달
        model.addAttribute("noticeDTO", noticeDTO);
        log.info("화면 이동 제목 : ",noticeDTO.getTitle());
        return "cs/notice/view";
    }

}
