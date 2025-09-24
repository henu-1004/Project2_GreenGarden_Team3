package kr.co.greengarden.repository;

import kr.co.greengarden.entity.MemberGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberGeneralRepository 작성
 */
@Repository
public interface MemberGeneralRepository extends JpaRepository<MemberGeneral, String> {

}
