package kr.co.greengarden.controller.admin.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminCouponController {

    @GetMapping("/admin/coupon/list")
    public String couponListPage() {
        return "admin/coupon/list";
    }

    @GetMapping("/admin/coupon/issued")
    public String couponIssuedPage() {
        return "admin/coupon/issued";
    }

}
