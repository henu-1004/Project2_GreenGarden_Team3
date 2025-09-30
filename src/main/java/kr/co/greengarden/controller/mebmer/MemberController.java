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
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberController 설정
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
    public String signup() {
        return "member/register";
    }

    @GetMapping("/member/registerSeller")
    public String signupSeller() {
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
