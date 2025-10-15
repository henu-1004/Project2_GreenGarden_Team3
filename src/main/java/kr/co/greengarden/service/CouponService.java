package kr.co.greengarden.service;
/*
*   날짜 : 2025/10/15
*   이름 : 이수연
*   내용 : 관리자 쿠폰 - 등록 구현
*/

import kr.co.greengarden.entity.Coupon;
import kr.co.greengarden.mapper.CouponMapper;
import kr.co.greengarden.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor // Repository를 주입받기 위함
public class CouponService {

    private final CouponRepository couponRepository; // JPA
    private final CouponMapper couponMapper;
    private final ModelMapper modelMapper;

    // --- 쿠폰 종류별 식별 코드 (상수) ---
    private static final String TYPE_INDIVIDUAL = "1";
    private static final String TYPE_ORDER = "2";
    private static final String TYPE_SHIPPING = "3";

    // ----------------------------------------------------------------------
    // 1. 등록 (C: Create)
    // ----------------------------------------------------------------------
    public void save() {

        // 1. 중독되지 않는 11자리 쿠폰 번호 생성

        Coupon coupon;

    }

    private String generateCouponNo() { return null; }
}
