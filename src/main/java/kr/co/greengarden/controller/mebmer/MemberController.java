package kr.co.greengarden.controller.mebmer;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.MemberSellerDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Terms;
import kr.co.greengarden.service.MemberGeneralService;
import kr.co.greengarden.service.MemberSellerService;
import kr.co.greengarden.service.MemberService;
import kr.co.greengarden.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * 날짜 : 2025/10/02
 * 이름 : 이종봉
 * 내용 : 약관 기능구현.
 */
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberGeneralService memberGeneralService;
    private final MemberSellerService memberSellerService;
    private final TermsService termsService;

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/join")
    public String join() {
        return "member/join";
    }

    @GetMapping("/member/register")
    public String register() {
        return "member/register";
    }

    @GetMapping("/member/signup")
    public String signup(@RequestParam String role, Model model) {

        String type = null;

        if(role.equals("general")) {
            type = "USER";
            model.addAttribute("type", "user");
        } else if(role.equals("seller")) {
            type = "SELLER";
            model.addAttribute("type", "seller");
        }

        Terms terms = termsService.getTermsByMemberType(type);
        model.addAttribute("terms", terms);

        return "member/signup";
    }



    @GetMapping("/member/registerSeller")
    public String registerSeller() {
        return "member/registerSeller";
    }

    @PostMapping("/member/register")
    public String signup(MemberDTO memberDTO, MemberGeneralDTO memberGeneralDTO) {

        memberGeneralService.save(memberDTO, memberGeneralDTO);

        return "redirect:/member/login";
    }

    @PostMapping("/member/registerSeller")
    public String signupSeller(MemberDTO memberDTO, MemberSellerDTO memberSellerDTO) {

        memberSellerService.save(memberDTO, memberSellerDTO);

        return "redirect:/member/login";
    }
}
