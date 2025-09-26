package kr.co.greengarden.repository;

import kr.co.greengarden.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderItemRepository 작성
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
