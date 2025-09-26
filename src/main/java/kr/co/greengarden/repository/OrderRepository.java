package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderRepository 작성
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

}
