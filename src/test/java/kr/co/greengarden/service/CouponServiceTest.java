package kr.co.greengarden.service;

import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("오라클 시퀀스를 사용한 11자리 쿠폰이 정상 등록되어야 한다.")
    void save() {

        // 1. Given (준비): 등록할 DTO 데이터 준비 (혜택, 발급처 등 포함)
        CouponDTO inputDto = new CouponDTO();
        inputDto.setType("개별상품할인");
        inputDto.setName("테스트 등록 쿠폰");
        inputDto.setBenefit("20% 할인"); // 혜택 반영
        inputDto.setIssuer("test_admin");
        inputDto.setIssueCount(50);
        inputDto.setStartDate(LocalDateTime.now().plusDays(1));
        inputDto.setEndDate(LocalDateTime.now().plusDays(30));

        // 2. When (실행): Service의 등록 메서드 호출
        CouponDTO resultDto = couponService.save(inputDto);

        // 3. Then (검증):

        // A. 반환된 DTO의 기본값 검증
        assertNotNull(resultDto, "반환된 DTO는 NULL이 아니어야 한다.");
        assertNotNull(resultDto.getCouponNo(), "쿠폰 번호가 자동 생성되어야 한다.");
        assertEquals("ISSUED", resultDto.getStatus(), "초기 상태는 'ISSUED'여야 한다.");

        // B. 쿠폰 번호 형식(11자리) 검증
        String couponNo = resultDto.getCouponNo();
        assertEquals(11, couponNo.length(), "쿠폰 번호는 11자리여야 한다.");

        // C. DB에 실제로 저장되었는지 Repository를 통해 최종 검증
        couponRepository.findById(couponNo)
                .ifPresentOrElse(
                        savedCoupon -> {
                            // DB에 저장된 값이 DTO 요청값과 일치하는지 확인
                            assertThat(savedCoupon.getName()).isEqualTo("테스트 등록 쿠폰");
                            assertThat(savedCoupon.getBenefit()).isEqualTo("20% 할인"); // 혜택 필드 검증
                            assertThat(savedCoupon.getIssuedAt()).isNotNull(); // 발급 시각이 기록되었는지 확인
                        },
                        // DB에 데이터가 없으면 테스트 실패
                        () -> fail("DB에서 쿠폰 번호 " + couponNo + "를 찾을 수 없습니다.")
                );
    }
}