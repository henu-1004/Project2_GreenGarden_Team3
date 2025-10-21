package kr.co.greengarden.repository;

import jakarta.persistence.LockModeType;
import kr.co.greengarden.entity.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long> {

    // 발급수 = ISSUED + USED
    long countByCouponNoAndStatusIn(String couponNo, Collection<String> statuses);

    // 사용수 = USED
    long countByCouponNoAndStatus(String couponNo, String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ci from CouponIssue ci where ci.issueId = :issueId")
    Optional<CouponIssue> findByIdForUpdate(@Param("issueId") String issueId);

    // 발급현황 리스트(표시 상태/버튼 활성 여부까지 SQL에서 계산)
    @Query(value = """
  SELECT
       i.ISSUE_ID              AS issueId,
       i.COUPON_NO             AS couponNo,
       c.TYPE                  AS type,
       c.NAME                  AS name,
       i.USER_ID               AS userId,
       i.USED_AT               AS usedAt,
       i.STATUS                AS issueStatus,
       CASE
         WHEN i.STATUS = 'CANCELLED'                 THEN '중단'
         WHEN i.STATUS = 'USED'                      THEN '사용'
         WHEN c.END_DATE < SYSTIMESTAMP              THEN '만료'
         ELSE '미사용'
       END                     AS viewStatus,
       CASE
         WHEN i.STATUS = 'ISSUED'
          AND (c.END_DATE IS NULL OR c.END_DATE >= SYSTIMESTAMP) THEN 1
         ELSE 0
       END                     AS canStop
  FROM TB_COUPON_ISSUE i
  JOIN TB_COUPON c ON c.COUPON_NO = i.COUPON_NO
  ORDER BY i.ISSUE_ID DESC
""", nativeQuery = true)
    java.util.List<kr.co.greengarden.repository.IssuedRow> findIssuedRows();


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
