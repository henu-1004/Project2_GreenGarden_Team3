package kr.co.greengarden.repository;

import kr.co.greengarden.entity.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, String> {

    @Query("select ci from CouponIssue ci " +
            "join fetch ci.coupon c " +
            "join fetch ci.member m " +
            "where m.memId = :memId")
    List<CouponIssue> findAllByMemberWithCoupon(@Param("memId") String memId);
}
