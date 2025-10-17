package kr.co.greengarden.repository;

import kr.co.greengarden.dto.admin.DeliveryDTO;
import kr.co.greengarden.dto.admin.AdminIndexOrderInfoDTO;
import kr.co.greengarden.dto.admin.AdminOrderListDTO;
import kr.co.greengarden.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    @Query("SELECT new kr.co.greengarden.dto.admin.AdminOrderListDTO(" +
            "o.orderNo, m.memId, g.name, COALESCE(oi.quantity, 0), o.totalPrice, o.payMethod, o.status, o.orderedAt) " +
            "FROM Order o " +
            "JOIN o.member m " +
            "JOIN m.general g " +
            "LEFT JOIN o.orderItems oi")
    List<AdminOrderListDTO> findAllAdminOrderList();

    String orderNo(String orderNo);


    // 관리자 인덱스용
    @Query("SELECT new kr.co.greengarden.dto.admin.AdminIndexOrderInfoDTO(" +
           "COALESCE(o.status, '미지정'), o.totalPrice) " +
           "FROM Order o")
    List<AdminIndexOrderInfoDTO> findAdminIndexOrderInfo();

    @Query(
            value = """
                      SELECT new kr.co.greengarden.dto.admin.AdminOrderListDTO(
                        o.orderNo, m.memId, g.name, COALESCE(oi.quantity, 0), o.totalPrice, o.payMethod, o.status, o.orderedAt
                      )
                      from Order o
                      join o.member m
                      join m.general g 
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
                    """
    )
    Page<AdminOrderListDTO> findAllOrderBySearch(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query(
            value = """
                      SELECT new kr.co.greengarden.dto.admin.DeliveryDTO(
                        d.deliveryId, d.order.orderNo, d.invoiceNo, d.status, d.cratedAt, d.note
                      )
                      from Delivery d
                      where
                        (:keyword is null or :keyword = '')
                        or (
                          ( :searchType is null or :searchType = '' ) and (
                            lower(d.invoiceNo)    like lower(concat('%', :keyword, '%')) or
                            lower(d.order.orderNo)  like lower(concat('%', :keyword, '%')) or
                            lower(d.note) like lower(concat('%', :keyword, '%'))
                          )
                        )
                        or (:searchType = 'invoiceNo'    and lower(d.invoiceNo)    like lower(concat('%', :keyword, '%')))
                        or (:searchType = 'orderNo'  and lower(d.order.orderNo)  like lower(concat('%', :keyword, '%')))
                        or (:searchType = 'name' and lower(d.note) like lower(concat('%', :keyword, '%')))
                    """
    )
    Page<DeliveryDTO> findAllDeliveryBySearch(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable);

}
