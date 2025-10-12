package kr.co.greengarden.controller.member;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.MemberSellerDTO;
import kr.co.greengarden.entity.Terms;
import kr.co.greengarden.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 * 날짜 : 2025/10/02
 * 이름 : 이종봉
 * 내용 : 약관 기능구현.
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberGeneralService memberGeneralService;
    private final MemberSellerService memberSellerService;
    private final TermsService termsService;
    private final EmailService emailService;

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

    // API 요청 메서드 - 추가
    @ResponseBody
    @GetMapping("/member/{type}/{value}")
    public ResponseEntity<Map<String, Integer>> getMemberCount(@PathVariable("type") String type,
                                                               @PathVariable("value") String value) {

        log.info("type = {}, value = {}", type, value);

        int count = memberService.countMember(type, value);

        // Json 생성
        Map<String, Integer> map = Map.of("count", count);

        return ResponseEntity.ok(map);

    }


}
