package kr.co.greengarden.controller;


import kr.co.greengarden.entity.Member;
import kr.co.greengarden.security.MemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MainController 설정
 */
@Controller
public class MainController {

    @GetMapping(value = {"/", "/index"})
    public String index(Authentication authentication, Model model) {

        if(authentication != null) {

            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
            Member member = memberDetails.getMember();

            model.addAttribute("member", member);
        }

        return "index";
    }

    /*
     * 날짜 : 2025/09/26
     * 이름 : 박효빈
     * 내용 : MainController + cs index page 추가 연결
     */
    @GetMapping(value = {"/cs"})
    public String csindexpage() {


        return "cs/cs_index";
    }
}