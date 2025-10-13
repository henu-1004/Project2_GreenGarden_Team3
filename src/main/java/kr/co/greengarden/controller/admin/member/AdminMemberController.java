package kr.co.greengarden.controller.admin.member;

import kr.co.greengarden.dto.GradeDTO;
import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.admin.MemberGeneralListDTO;
import kr.co.greengarden.dto.admin.MemberGerneralModifyDTO;
import kr.co.greengarden.entity.Grade;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberGeneral;
import kr.co.greengarden.entity.Point;
import kr.co.greengarden.service.GradeService;
import kr.co.greengarden.service.MemberGeneralService;
import kr.co.greengarden.service.MemberSellerService;
import kr.co.greengarden.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.management.GarbageCollectorMXBean;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;
    private final MemberGeneralService memberGeneralService;
    private final MemberSellerService memberSellerService;
    private final GradeService gradeService;

    @GetMapping("/admin/member/list")
    public String memberSearch(@Param("searchType") String searchType,
                               @Param("keyword") String keyword,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {

        Page<MemberGeneralListDTO> memberGeneralList = memberGeneralService.findGeneralBySearch(searchType, keyword, page, 5);
        System.out.println(searchType + "," + keyword);

        System.out.println(memberGeneralList.toString());
        model.addAttribute("memberGeneralList", memberGeneralList);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/modify")
    public String modify(@RequestParam("memId") String memId, Model model) {
        MemberGerneralModifyDTO memberGeneral = memberGeneralService.findGeneralById(memId);
        model.addAttribute("memberGeneral", memberGeneral);
        return "admin/member/modify";
    }

    @GetMapping("/admin/member/point")
    public String point() {
        return "admin/member/point"; // templates/admin/member/point.html
    }

    @PostMapping("/admin/member/modify")
    public String modify(MemberDTO memberData, MemberGeneralDTO generalData, GradeDTO gradeData) {
        Optional<Member> optionalMember = memberService.getUser(memberData.getMemId());
        Member member = optionalMember.get();
        MemberDTO memberDTO = member.toDTO();

        Optional<MemberGeneral> optionalGeneral = memberGeneralService.getUser(member.getMemId());
        MemberGeneral general = optionalGeneral.get();
        MemberGeneralDTO generalDTO = general.toDTO();

        Optional<Grade> optionalGrade = gradeService.getGrade(member.getMemId());
        Grade grade = optionalGrade.get();
        GradeDTO gradeDTO = grade.toDTO();

        memberDTO.setZipCode(memberData.getZipCode());
        memberDTO.setAddressBasic(memberData.getAddressBasic());
        memberDTO.setAddressDetail(memberData.getAddressDetail());

        generalDTO.setStatus(generalData.getStatus());
        generalDTO.setName(generalData.getName());
        generalDTO.setGender(generalData.getGender());
        generalDTO.setEmail(generalData.getEmail());
        generalDTO.setPhone(generalData.getPhone());
        generalDTO.setNote(generalData.getNote());

        gradeDTO.setGrade(gradeData.getGrade());

        memberGeneralService.modifyGeneral(member, general, grade);

        return "redirect:admin/member/list";
    }

    @PostMapping("/admin/member/deleteSelected")
    public String deleteSelected(@RequestParam("memIds") List<String> memIds,
                                 RedirectAttributes ra) {
        if (memIds == null || memIds.isEmpty()) {
            ra.addFlashAttribute("msg", "선택된 항목이 없습니다.");
            return "redirect:admin/member/list";
        }
        memberGeneralService.deleteMembers(memIds);
        ra.addFlashAttribute("msg", memIds.size() + "건 삭제되었습니다.");
        return "redirect:admin/member/list";
    }

}