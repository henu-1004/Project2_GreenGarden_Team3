package kr.co.greengarden.controller.cs;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
 * 날짜 : 2025/09/26
 * 이름 : 박효빈
 * 내용 : FaqController  추가
 */
@Controller
public class FaqController {

    @GetMapping("/cs/faq/list")
    public String list(){
        return "cs/faq/list";
    }

    @GetMapping("/cs/faq/view")
    public String view(){
        return "cs/faq/view";
    }
}
