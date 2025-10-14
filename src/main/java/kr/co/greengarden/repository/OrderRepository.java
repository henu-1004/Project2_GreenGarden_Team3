package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
