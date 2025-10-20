package kr.co.greengarden.controller.admin.config;

import kr.co.greengarden.dto.admin.TermsModifyDTO;
import kr.co.greengarden.entity.Terms;
import kr.co.greengarden.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminConfigController {

    private final TermsService termsService;

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
    public String policyListPage(Model model) {
        Terms buyer = termsService.getTermsByMemberType("USER");
        Terms seller = termsService.getTermsByMemberType("SELLER");

        model.addAttribute("buyerTerms", safe(buyer != null ? buyer.getTermsUse() : null));
        model.addAttribute("sellerTerms", safe(seller != null ? seller.getTermsUse() : null));
        model.addAttribute("financeTerms", safe(termsService.getFinanceTerms()));
        model.addAttribute("privacyTerms", safe(termsService.getPrivacyTerms()));
        model.addAttribute("locationTerms", safe(termsService.getLocationTerms()));

        return "admin/config/policy"; // templates/admin/config/policy.html 렌더링
    }

    @PostMapping("/admin/config/policy")
    public String updatePolicy(@ModelAttribute TermsModifyDTO form,
                               RedirectAttributes ra){
        termsService.update(form);
        ra.addFlashAttribute("msg", "저장되었습니다.");
        return "redirect:/admin/config/policy";

    }

    @GetMapping("/admin/config/version")
    public String versionListPage() {
        return "admin/config/version"; // templates/admin/config/version.html 렌더링
    }


    private String safe(String s){
        return (s == null) ? "" : s;
    }
}
