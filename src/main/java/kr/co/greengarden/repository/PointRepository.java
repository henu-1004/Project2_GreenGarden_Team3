package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : PointRepository 작성
 */
@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Point p where p.member.memId in :ids")
    void deleteByMemberIdIn(@Param("ids") List<String> ids);

}
