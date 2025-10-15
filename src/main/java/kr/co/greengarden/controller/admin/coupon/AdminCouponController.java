package kr.co.greengarden.controller.admin.coupon;

import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    @GetMapping("/admin/coupon/list")
    public String couponListPage() {
        return "admin/coupon/list";
    }

    @GetMapping("/admin/coupon/issued")
    public String couponIssuedPage() {
        return "admin/coupon/issued";
    }

    @PostMapping
    public ResponseEntity<CouponDTO> registerCoupon(@RequestBody CouponDTO requestDto) {

        // (필수 유효성 검증 로직 추가)
        try {
            // Service 호출 (등록 처리 및 최종 결과 DTO 반환)
            CouponDTO registeredCoupon = couponService.save(requestDto);

            // 201 Created 상태와 최종 DTO를 반환
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(registeredCoupon);

        } catch (Exception e) {
            // 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
