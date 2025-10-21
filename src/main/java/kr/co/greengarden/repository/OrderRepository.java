package kr.co.greengarden.repository;

import kr.co.greengarden.dto.admin.*;
import kr.co.greengarden.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderRepository 작성
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    // 특정 회원(memId)의 모든 주문 조회
    List<Order> findAllByMember_MemId(String id);

    // 특정 회원(memId)의 최근 5건 주문 조회
    List<Order> findTop5ByMember_MemIdOrderByOrderedAtDesc(String id);

    /*
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.member m " +
            "JOIN FETCH m.general g " +
            "LEFT JOIN FETCH o.orderItems oi")
     */
    @Query("""
                select new kr.co.greengarden.dto.admin.AdminProductListDTO(
                         p.proId, p.img1, p.proNo, p.name, p.price, p.discountRate, p.point, p.stock, s.company, p.views
                      )
                from Product p
                join p.seller s
            """)
    List<AdminOrderListDTO> findAllAdminOrderDetailList();

    String orderNo(String orderNo);


    // 관리자 인덱스용
    @Query("SELECT new kr.co.greengarden.dto.admin.AdminIndexOrderInfoDTO(" +
            "COALESCE(o.status, '미지정'), o.totalPrice) " +
            "FROM Order o")
    List<AdminIndexOrderInfoDTO> findAdminIndexOrderInfo();

    @Query(
            value = """
                      SELECT new kr.co.greengarden.dto.admin.AdminOrderListDTO(
                        o.orderNo, m.memId, g.name, COALESCE(SUM(oi.quantity), 0L), o.totalPrice, o.payMethod,
                        o.status, o.orderedAt, COALESCE((SELECT d.status FROM Delivery d
                                                     WHERE d.order = o
                                                     ORDER BY d.createdAt DESC
                                                     LIMIT 1), '')
                      )
                      from Order o
                      join o.member m
                      join m.general g 
                      left join Delivery d ON d.order = o
                      left join o.orderItems oi
                      where
                        (:keyword is null or :keyword = '')
                        or (
                          ( :searchType is null or :searchType = '' ) and (
                            lower(o.orderNo)    like lower(concat('%', :keyword, '%')) or
                            lower(m.memId)  like lower(concat('%', :keyword, '%')) or
                            lower(g.name) like lower(concat('%', :keyword, '%'))
                          )
                        )
                        or (:searchType = 'orderNo'    and lower(o.orderNo)    like lower(concat('%', :keyword, '%')))
                        or (:searchType = 'memId'  and lower(m.memId)  like lower(concat('%', :keyword, '%')))
                        or (:searchType = 'name' and lower(g.name) like lower(concat('%', :keyword, '%')))
                      GROUP BY
                        o.orderNo, m.memId, g.name, o.totalPrice, o.payMethod, o.status, o.orderedAt, d.status
                    """
    )
    Page<AdminOrderListDTO> findAllOrderBySearch(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("""
        select new kr.co.greengarden.dto.admin.DeliveryListDTO(
          d.invoiceNo, d.company, o.orderNo, o.recName, MIN(p.name), 
          CAST(COALESCE(SUM(oi.quantity), 0) AS long),
          CAST(o.totalPrice AS integer),
          CAST(MAX(oi.deliveryFee) AS long),
          COALESCE(d2.status, ''), 
          d.createdAt
        )
        from Delivery d
        join d.order o
        join o.orderItems oi                         
        join oi.product p
        left join Delivery d2
          on d2.order = o
         and d2.createdAt = (
            select max(d3.createdAt) from Delivery d3 where d3.order = o
         )
        where
          (
            (:keyword is null or :keyword = '')
            or (
              (:searchType is null or :searchType = '') and (
                lower(d.invoiceNo) like lower(concat('%', :keyword, '%')) or
                lower(o.orderNo)   like lower(concat('%', :keyword, '%')) or
                lower(d.note)      like lower(concat('%', :keyword, '%'))
              )
            )
            or (:searchType = 'invoiceNo' and lower(d.invoiceNo) like lower(concat('%', :keyword, '%')))
            or (:searchType = 'orderNo'   and lower(o.orderNo)   like lower(concat('%', :keyword, '%')))
            or (:searchType = 'name'      and lower(o.recName)   like lower(concat('%', :keyword, '%')))
          )
        group by
          d.invoiceNo, d.company, o.orderNo, o.recName, o.totalPrice, d.createdAt, d2.status
        order by d.createdAt desc
        """)
    Page<DeliveryListDTO> findAllDeliveryBySearch(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("""
              SELECT new kr.co.greengarden.dto.admin.DeliveryInputDTO(
                o.orderNo, o.recName, o.recZipCode, o.recAddressBasic, o.recAddressDetail,
                COALESCE(d.company, ''), COALESCE(d.invoiceNo, ''), COALESCE(d.note, '')
              )
              FROM Order o
              LEFT JOIN Delivery d ON d.order = o
              WHERE o.orderNo = :orderNo
            """)
    DeliveryInputDTO findDeliveryInfo(@Param("orderNo") String orderNo);


    @Query("""
            SELECT new kr.co.greengarden.dto.admin.AdminOrderDetailListDTO(
                p.img1, p.proNo, p.name, s.company, oi.price, oi.discountRate, oi.quantity, oi.deliveryFee, p.point,
                o.orderedAt, o.orderNo, g.name, m.zipCode, m.addressBasic, m.addressDetail, g.phone,
                o.recName, o.recZipCode, o.recAddressBasic, o.recAddressDetail, o.recPhone
            )
                FROM OrderItem oi
                JOIN oi.order   o
                JOIN oi.product p
                JOIN p.seller   s
                JOIN o.member   m
                JOIN m.general  g
                WHERE o.orderNo = :orderNo
            """)
    List<AdminOrderDetailListDTO> findOrderDetailList(String orderNo);


}
