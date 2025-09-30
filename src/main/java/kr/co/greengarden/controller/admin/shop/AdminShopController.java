package kr.co.greengarden.controller.admin.shop;

import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.service.MemberSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminShopController {

    private final MemberSellerService memberSellerService;

    @GetMapping("/admin/shop/list")
    public String shopList(@RequestParam(required = false) String searchType,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {

        Page<MemberSeller> shopList = memberSellerService.findShopBySearch(searchType, keyword, page, 5);
        model.addAttribute("shopList", shopList);
        return "admin/shop/list";
    }

    @GetMapping("/admin/shop/register")
    public String shopRegister() {
        return "/admin/shop/register";
    }

    // 선택 삭제
    @PostMapping("/admin/shop/deleteSelected")
    public String deleteSelected(@RequestParam("memIds") List<String> memIds) {
        memberSellerService.deleteShops(memIds);
        return "redirect:/admin/shop/list";
    }

}