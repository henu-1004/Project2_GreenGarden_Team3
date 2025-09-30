package kr.co.greengarden.controller.mebmer;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.MemberSellerDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.service.MemberGeneralService;
import kr.co.greengarden.service.MemberSellerService;
import kr.co.greengarden.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/*
 * 날짜 : 2025/09/30
 * 이름 : 이종봉
 * 내용 : 판매자회원가입, 약관 controller 추가 및 일반회원가입 controller 수정
 */
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberGeneralService memberGeneralService;
    private final MemberSellerService memberSellerService;

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
    public String signup() {
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
