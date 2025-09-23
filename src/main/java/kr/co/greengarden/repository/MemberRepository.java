package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberRepository 작성
 */
public interface MemberRepository extends JpaRepository<Member,String> {

}
