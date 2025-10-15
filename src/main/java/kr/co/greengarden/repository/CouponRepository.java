package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    // 쿠폰 번호 중복 확인 로직을 위한 쿼리 메서드 (JPA가 메서드 이름으로 자동 구현)
    boolean existsByCouponNo(String couponNo);
}
