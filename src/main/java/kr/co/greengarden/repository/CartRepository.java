package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.MemberGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : CartRepository 작성
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}
