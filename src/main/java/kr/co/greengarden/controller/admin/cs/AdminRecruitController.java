package kr.co.greengarden.controller.admin.cs;

import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.dto.RecruitDTO;
import kr.co.greengarden.service.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * 이름 : 박효빈
 * 날짜 : 2025/10/19
 * 내용 : 관리자 채용공고 컨트롤러 (목록, 상세, 등록)
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminRecruitController {

    private final RecruitService recruitService;

    /**
     * 채용 목록 페이지 (페이징, 검색 포함)
     * URL: /admin/cs/recruit/list
     */
    @GetMapping("/admin/cs/recruit/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("Admin Recruit List 요청: {}", pageRequestDTO);

        PageResponseDTO<RecruitDTO> responseDTO = recruitService.getRecruitList(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        // ✅ 여기 경로 중요! — 슬래시(/) 없이
        return "admin/cs/recruit/list";
    }

    /**
     * 채용공고 상세 보기
     * URL: /admin/cs/recruit/view?recruitId=1
     */
    @GetMapping("/admin/cs/recruit/view")
    public String view(@RequestParam("recruitId") Integer recruitId, Model model) {
        RecruitDTO recruitDTO = recruitService.getRecruit(recruitId);

        if (recruitDTO == null) {
            log.warn("채용공고를 찾을 수 없습니다. recruitId={}", recruitId);
            return "redirect:/admin/cs/recruit/list";
        }

        model.addAttribute("recruitDTO", recruitDTO);
        return "admin/cs/recruit/view";
    }

    /**
     * 채용공고 등록 (POST)
     * URL: /admin/cs/recruit/register
     * list.html 내 모달 폼에서 등록
     */
    @PostMapping("/admin/cs/recruit/register")
    public String register(RecruitDTO recruitDTO, RedirectAttributes rttr) {
        int id = recruitService.registerRecruit(recruitDTO);
        rttr.addFlashAttribute("msg", "채용공고가 등록되었습니다!");
        log.info("채용 등록 완료 ID={}", id);

        return "redirect:/admin/cs/recruit/list";
    }

    /**
     * 채용공고 삭제
     * URL: /admin/cs/recruit/delete?recruitId=1
     */
    @PostMapping("/admin/cs/recruit/delete")
    public String delete(@RequestParam("recruitId") Integer recruitId, RedirectAttributes rttr) {
        recruitService.deleteRecruit(recruitId);
        rttr.addFlashAttribute("msg", "채용공고가 삭제되었습니다.");
        return "redirect:/admin/cs/recruit/list";
    }
}
