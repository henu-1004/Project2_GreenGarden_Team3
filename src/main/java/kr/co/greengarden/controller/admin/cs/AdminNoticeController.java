package kr.co.greengarden.controller.admin.cs;

import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminNoticeController {

    private final NoticeService noticeService;

    // 목록 보기 + 페이징 + 어쩌고저쩌고
    @GetMapping("/admin/cs/notice/list")
    public String adminCsList(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<NoticeDTO> pageResponseDTO = noticeService.getNoticesList(pageRequestDTO);

        //model에 담아 전달
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/cs/notice/list";
    }
    // 글보기
    @GetMapping("/admin/cs/notice/view")
    public String adminCsView(Integer noticeId, Model model) {

        NoticeDTO noticeDTO = noticeService.getNotice(noticeId);

        if (noticeDTO == null) {
            return "redirect:/admin/cs/notice/list";
        }
        model.addAttribute("noticeDTO", noticeDTO);

        return "admin/cs/notice/view";
    }

    // 수정 폼
    @GetMapping("/admin/cs/notice/modify")
    public String adminCsModify(int noticeId, Model model) {
        NoticeDTO noticeDTO = noticeService.getNotice(noticeId); // 조회수 증가하지않게 하기 위해 새로 생성

        if (noticeDTO == null) {
            return "redirect:/admin/cs/notice/list";
        }
        model.addAttribute("noticeDTO", noticeDTO);


        return "admin/cs/notice/modify";
    }
    // 수정 처리
    @PostMapping("/admin/cs/notice/modify")
    public String adminCsModifyProc(NoticeDTO noticeDTO) {

        noticeService.modifyNotice(noticeDTO);



        return "redirect:/admin/cs/notice/list";
    }
    @PostMapping("/admin/cs/notice/write")
    public String adminCsWrite(NoticeDTO noticeDTO) {

        int noticeId = noticeService.registerNotice(noticeDTO);
        return "redirect:/admin/cs/notice/view?noticeId=" + noticeId;
    }



}
