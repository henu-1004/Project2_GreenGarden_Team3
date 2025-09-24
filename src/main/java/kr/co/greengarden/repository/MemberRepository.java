package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberRepository 작성
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}
