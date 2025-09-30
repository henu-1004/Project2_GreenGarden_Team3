package kr.co.greengarden.controller.admin.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/shop")
public class AdminShopController {

    // 상점 목록 페이지
    @GetMapping("/list")
    public String shopList(Model model, Pageable pageable) {
        // service 호출 → shopList 가져오기
        Page<ShopDTO> shopList = shopService.getShopList(pageable);

        // 타임리프에 전달
        model.addAttribute("shopList", shopList);

        return "admin/shop/list"; // templates/admin/shop/list.html
    }
}