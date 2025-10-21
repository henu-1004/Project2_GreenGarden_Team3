package kr.co.greengarden.controller.admin.coupon;

import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/list")
    public String couponListPage(Model model) {

        //List<CouponDTO> couponList = couponService.getCouponList();
        List<CouponDTO> couponList = couponService.getCouponListWithCounts();

        model.addAttribute("couponList", couponList);

        return "admin/coupon/list";
    }

    @GetMapping("/issued")
    public String couponIssuedPage(Model model) {
        model.addAttribute("issuedList", couponService.getIssuedListSimple());
        return "admin/coupon/issued";
    }

    @PostMapping("/couponRegister")
    public String couponRegister(CouponDTO couponDTO) {

        couponService.register(couponDTO);

        return "redirect:/admin/coupon/list";
    }

    @PostMapping("/issued/{issueId}/stop")
    public String stopIssuedByForm(@PathVariable String issueId) {

        couponService.stopIssued(issueId);
        return "redirect:/admin/coupon/issued";
    }




}
