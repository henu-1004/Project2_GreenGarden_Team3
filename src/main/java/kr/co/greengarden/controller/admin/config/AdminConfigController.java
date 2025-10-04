package kr.co.greengarden.controller.admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminConfigController {

    @GetMapping("/admin/config/banner")
    public String bannerListPage() {
        return "admin/config/banner"; // templates/admin/config/banner.html 렌더링
    }

    @GetMapping("/admin/config/basic")
    public String basicListPage() {
        return "admin/config/basic"; // templates/admin/config/basic.html 렌더링
    }

    @GetMapping("/admin/config/category")
    public String categoryListPage() {
        return "admin/config/category"; // templates/admin/config/category.html 렌더링
    }

    @GetMapping("/admin/config/policy")
    public String policyListPage() {
        return "admin/config/policy"; // templates/admin/config/policy.html 렌더링
    }

    @GetMapping("/admin/config/version")
    public String versionListPage() {
        return "admin/config/version"; // templates/admin/config/version.html 렌더링
    }


}
