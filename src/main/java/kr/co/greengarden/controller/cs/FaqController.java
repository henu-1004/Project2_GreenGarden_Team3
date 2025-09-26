package kr.co.greengarden.controller.cs;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
