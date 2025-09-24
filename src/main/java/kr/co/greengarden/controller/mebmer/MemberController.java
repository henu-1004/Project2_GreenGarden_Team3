package kr.co.greengarden.controller.mebmer;

import kr.co.greengarden.dto.MemberDTO;
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

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/register")
    public String signup() {
        return "member/register";
    }

    @PostMapping("/member/register")
    public String signup(MemberDTO memberDTO) {

        memberService.save(memberDTO);

        return "redirect:/member/login";
    }
}
