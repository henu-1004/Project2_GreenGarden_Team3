package kr.co.greengarden.repository;

import kr.co.greengarden.entity.MemberSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberSellerRepository 작성
 */
@Repository
public interface MemberSellerRepository extends JpaRepository<MemberSeller, Integer> {
}
