package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/*
    날짜 : 2025/10/19
    이름 : 이수연
    내용 : admin index 집계 chart
*/

@Repository
public interface ChartRepository extends JpaRepository<Order, String> {

    // 특정 날짜의 주문 건수
    @Query("SELECT COUNT(o) " +
            "FROM Order o " +
            // 🌟 TRUNC 대신 TO_CHAR 사용!
            "WHERE FUNCTION('TO_CHAR', o.orderedAt, 'YYYY-MM-DD') = FUNCTION('TO_CHAR', :date, 'YYYY-MM-DD')")
    int countOrdersByDate(@Param("date") LocalDate date);

    // 특정 날짜의 결제 완료 건수 (오류났던 쿼리)
    @Query("SELECT COUNT(o) " +
            "FROM Order o " +
            "WHERE o.status IN ('결제완료', '배송준비', '배송중') " +
            // 🌟 TRUNC 대신 TO_CHAR 사용!
            "AND FUNCTION('TO_CHAR', o.orderedAt, 'YYYY-MM-DD') = FUNCTION('TO_CHAR', :date, 'YYYY-MM-DD')")
    int countPaymentsByDate(@Param("date") LocalDate date);

    // 특정 날짜의 취소 건수 (DISTINCT 유지)
    @Query("SELECT COUNT(DISTINCT o) " +
            "FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE oi.cancelYN = 'Y' " +
            // 🌟 TRUNC 대신 TO_CHAR 사용!
            "AND FUNCTION('TO_CHAR', o.orderedAt, 'YYYY-MM-DD') = FUNCTION('TO_CHAR', :date, 'YYYY-MM-DD')")
    int countCancelsByDate(@Param("date") LocalDate date);

    // 기간별 카테고리 매출(Native Query)
    @Query(value = "SELECT p.CATEGORY_SLUG, SUM(oi.PRICE * oi.QUANTITY) as TOTAL " +
            "FROM TB_ORDER_ITEM oi " +
            "JOIN TB_PRODUCT p ON oi.PROID = p.PROID " +
            "JOIN TB_ORDER o ON oi.ORDERNO = o.ORDERNO " +
            "WHERE o.STATUS != '취소' " +
            // 🌟 TRUNC 대신 TO_CHAR로 문자열 범위 비교!
            "AND TO_CHAR(o.ORDEREDAT, 'YYYY-MM-DD') BETWEEN TO_CHAR(:startDate, 'YYYY-MM-DD') AND TO_CHAR(:endDate, 'YYYY-MM-DD') " +
            "GROUP BY p.CATEGORY_SLUG " +
            "ORDER BY TOTAL DESC " +
            "FETCH FIRST 4 ROWS ONLY", nativeQuery = true)
    List<Object[]> findCategorySalesByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
