package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : GradeRepository 작성
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, String> {
    @Modifying
    @Query("delete from Grade gr where gr.memId in :ids")
    void deleteByMemIdIn(@Param("ids") List<String> ids);
}
