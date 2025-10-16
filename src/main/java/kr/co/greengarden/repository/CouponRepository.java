package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    // 오라클 시퀀스에서 다음 번호(NEXTVAL)를 가져오는 쿼리
    @Query(value = "SELECT COUPON_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
    Long getNextSequenceValue();

    // 전체 목록을 최신 등록순으로 가져오는 메서드
    List<Coupon> findAllByOrderByIssuedAtDesc();

    // 페이징 처리
    Page<Coupon> findAll(Pageable pageable);

}