package kr.co.greengarden.service;
/*
*   날짜 : 2025/10/15
*   이름 : 이수연
*   내용 : 관리자 쿠폰 - 등록 구현
*/

import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.entity.Coupon;
import kr.co.greengarden.mapper.CouponMapper;
import kr.co.greengarden.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor // Repository를 주입받기 위함
public class CouponService {

    private final CouponRepository couponRepository; // JPA
    private final ModelMapper modelMapper;

    // --- 쿠폰 종류별 식별 코드 (상수) ---
    private static final String TYPE_INDIVIDUAL = "1";
    private static final String TYPE_ORDER = "2";
    private static final String TYPE_SHIPPING = "3";
    private final CouponMapper couponMapper;

    // ----------------------------------------------------------------------
    // 1. 등록 (C: Create)
    // ----------------------------------------------------------------------
    public CouponDTO save(CouponDTO dto) {

        // 1. 중복되지 않는 11자리 쿠폰 번호 생성
        String newCouponNo;
        do {
            newCouponNo = generateCouponNo(dto.getCouponNo());
        } while (couponRepository.existsByCouponNo(newCouponNo));

        // 2. DTO -> Entity 변환 (ModelMapper 사용)
        Coupon coupon = modelMapper.map(dto, Coupon.class);

        // 3. Entity에 서버 설정 값 주입 (개별 set 메서드 필요)
        //    *주의*: Coupon.java에 setCouponNo, setIssuedAt, setStatus 메서드가 있어야 작동한다.
        coupon.setCouponNo(newCouponNo);
        coupon.setIssuedAt(LocalDateTime.now());
        coupon.setStatus("ISSUED");

        // 4. DB에 저장
        Coupon savedCoupon = couponRepository.save(coupon);

        // 5. 저장된 Entity -> DTO로 변환하여 반환 (응답)
        return modelMapper.map(savedCoupon, CouponDTO.class);
    }

    // ----------------------------------------------------------------------
    // [보조] 11자리 쿠폰 번호 생성 로직
    // ----------------------------------------------------------------------
    private String generateCouponNo(String couponNo) {
        String NomCode = switch (couponNo) {
            case "개별상품할인" -> TYPE_INDIVIDUAL;
            case "주문상품할인" -> TYPE_ORDER;
            case "배송비무료" -> TYPE_SHIPPING;
            default -> "0";
        };
        String dateCode = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));
        Random random = new Random();
        int uniqueNum = random.nextInt(900000) + 100000;
        return NomCode + dateCode + String.valueOf(uniqueNum);
    }

}
