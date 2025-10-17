package kr.co.greengarden.controller.admin.coupon;

import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/list")
    public String couponListPage(Model model) {

        List<CouponDTO> couponList = couponService.getCouponList();

        model.addAttribute("couponList", couponList);

        return "admin/coupon/list";
    }

    @GetMapping("/issued")
    public String couponIssuedPage() {
        return "admin/coupon/issued";
    }

    @PostMapping("/couponRegister")
    public String couponRegister(CouponDTO couponDTO) {

        couponService.register(couponDTO);

        return "redirect:/admin/coupon/list";
    }


}
