package kr.co.greengarden.controller.policy;

import kr.co.greengarden.entity.Terms;
import kr.co.greengarden.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class PolicyController {

    private final TermsService termsService;

    @GetMapping("/policy")
    public String policyRoot(){
        return "redirect:/policy/buyer";
    }

    @GetMapping("/policy/buyer")
    public String buyer(Model model) {
        Terms t = termsService.getTermsByMemberType("USER");
        model.addAttribute("title", "구매회원 이용약관");
        model.addAttribute("content", t !=null ? t.getTermsUse() : "등록된 구매회원 약관이 없습니다.");
        model.addAttribute("tab", "buyer");
        return "policy/buyer";
    }

    @GetMapping("/policy/seller")
    public String seller(Model model) {
        Terms t = termsService.getTermsByMemberType("SELLER");
        model.addAttribute("title", "판매회원 이용약관");
        model.addAttribute("content", t !=null ? t.getTermsUse() : "등록된 판매회원 약관이 없습니다.");
        model.addAttribute("tab", "seller");
        return "policy/seller";
    }

    @GetMapping("/policy/finance")
    public String finance(Model model) {
        String content = termsService.getFinanceTerms();
        model.addAttribute("title", "전자금융거래 이용약관");
        model.addAttribute("content", content !=null ? content : "등록된 전자금융거래 약관이 없습니다.");
        model.addAttribute("tab", "finance");
        return "policy/finance";
    }

    @GetMapping("/policy/location")
    public String location(Model model) {
        String content = termsService.getLocationTerms();
        model.addAttribute("title", "위치정보 이용약관");
        model.addAttribute("content", content !=null ? content : "등록된 위치정보 약관이 없습니다.");
        model.addAttribute("tab", "location");
        return "policy/location";
    }

    @GetMapping("/policy/privacy")
    public String privacy(Model model) {
        String content = termsService.getPrivacyTerms();
        model.addAttribute("title", "개인정보 이용약관");
        model.addAttribute("content", content !=null ? content : "등록된 개인정보 약관이 없습니다.");
        model.addAttribute("tab", "privacy");
        return "policy/privacy";
    }
}
