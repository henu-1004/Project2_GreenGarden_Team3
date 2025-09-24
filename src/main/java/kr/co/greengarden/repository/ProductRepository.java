package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : ProductRepository 작성
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Modifying
    @Query("delete from Product p where p.proId in :ids")
    void deleteByProductIdIn(@Param("ids") List<Integer> ids);
}
