package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberRepository 작성
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    public int countByMemId(String memId);

    // 비밀번호 변경
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Member m set m.password = :pw where m.memId = :id")
    int updatePassword(@Param("id") String memId,
                       @Param("pw") String encodedPassword);

}
