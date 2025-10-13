package kr.co.greengarden.controller.admin.cs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminNoticeController {

    @GetMapping("/admin/cs/notice/list")
    public String adminCsList() {
        return "admin/cs/notice/list";
    }

    @GetMapping("/admin/cs/notice/view")
    public String adminCsView() {
        return "admin/cs/notice/view";
    }
    @GetMapping("/admin/cs/notice/modify")
    public String adminCsmodify() {
        return "admin/cs/notice/modify";
    }
    @GetMapping("/admin/cs/notice/write")
    public String adminCswrite() {
        return "admin/cs/notice/write";
    }



}
