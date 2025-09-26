package kr.co.greengarden.controller.cs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @GetMapping("/cs/notice/list")
    public String list(){
        return "cs/notice/list";
    }

    @GetMapping("/cs/notice/view")
    public String view(){
        return "cs/notice/view";
    }

}
