package kr.co.greengarden.controller.cs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
 * 날짜 : 2025/09/26
 * 이름 : 박효빈
 * 내용 : NoticeController 추가 연결
 */
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
