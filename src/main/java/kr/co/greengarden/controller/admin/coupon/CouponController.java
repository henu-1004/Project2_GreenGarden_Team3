package kr.co.greengarden.controller.admin.coupon;

import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.entity.Coupon;
import kr.co.greengarden.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/list")
    public String couponListPage() {
        return "admin/coupon/list";
    }

    @GetMapping("/issued")
    public String couponIssuedPage() {
        return "admin/coupon/issued";
    }

    @PostMapping("/couponRegister")
    public ResponseEntity<CouponDTO> couponRegister(@RequestBody CouponDTO requestDto) {

        // (필수 유효성 검증 로직 추가)
        try {
            // Service 호출 (등록 처리 및 최종 결과 DTO 반환)
            CouponDTO registeredCoupon = couponService.register(requestDto);

            // 201 Created 상태와 최종 DTO를 반환
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(registeredCoupon);

        } catch (Exception e) {
            // 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

}
