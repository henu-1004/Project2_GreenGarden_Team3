package kr.co.greengarden.repository;

import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.MemberGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : CartRepository 작성
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT new kr.co.greengarden.dto.CartListDTO(c.quantity, p.name, p.description, p.img1, p.price, p.discountRate, p.point, p.deliveryFee) " +
            "FROM Cart c " +
            "JOIN c.product p " +
            "WHERE c.member.memId = :memId")
    List<CartListDTO> findCartListByMemId(String memId);

    public List<Cart> findAllByMember_MemId(String memId);
}
