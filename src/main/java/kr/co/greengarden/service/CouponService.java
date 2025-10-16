package kr.co.greengarden.service;
/*
*   날짜 : 2025/10/15
*   이름 : 이수연
*   내용 : 관리자 쿠폰 - 등록 구현
*/

import jakarta.transaction.Transactional;
import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.entity.Coupon;
import kr.co.greengarden.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor // Repository를 주입받기 위함
public class CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    // --- 쿠폰 종류별 식별 코드 (상수) ---
    private static final String TYPE_INDIVIDUAL = "1";  // 개별상품할인
    private static final String TYPE_ORDER = "2";       // 주문상품할인
    private static final String TYPE_SHIPPING = "3";    // 배송비 무료

    // ----------------------------------------------------------------------
    //  11자리 쿠폰 번호 조합 로직 (타입 + 년월 + 시퀀스)
    // ----------------------------------------------------------------------
    private String generateCouponNo(String couponType) {

        // 1. 타입 코드 (1자리)
        String typeCode = switch (couponType) {
            case "개별상품할인" -> TYPE_INDIVIDUAL;
            case "주문상품할인" -> TYPE_ORDER;
            case "배송비 무료" -> TYPE_SHIPPING;
            default -> "0";
        };

        // 2. 년월 코드 (4자리: YYMM)
        String dateCode = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));

        // 3. 순차 번호 (6자리: 오라클 SEQUENCE 사용)
        Long nextVal = couponRepository.getNextSequenceValue();
        // %06d: 숫자를 무조건 6자리로 만들고, 앞자리는 0으로 채운다.
        String uniqueCode = String.format("%06d", nextVal);

        // 4. 11자리 최종 조합
        return typeCode + dateCode + uniqueCode;
    }

    // ----------------------------------------------------------------------
    // 1. 등록 (C: Create)
    // ----------------------------------------------------------------------
    @Transactional
    public CouponDTO register(CouponDTO dto) {

        // 1. 쿠폰 번호 조합 및 생성 (타입종류 + 년월 + 순차번호)
        String newCouponNo = generateCouponNo(dto.getType());

        // 2. DTO -> Entity 변환 (ModelMapper 사용)
        Coupon coupon = modelMapper.map(dto, Coupon.class);

        // 3. Entity에 서버 설정 값 주입
        coupon.setCouponNo(newCouponNo);
        coupon.setIssuedAt(LocalDateTime.now());
        coupon.setStatus("ISSUED"); // 발급 완료 상태로 설정

        // 4. DB에 저장
        Coupon savedCoupon = couponRepository.save(coupon);

        // 5. 저장된 Entity -> DTO로 변환하여 반환
        return modelMapper.map(savedCoupon, CouponDTO.class);
    }

}
