package kr.co.greengarden.repository;

import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : CartRepository 작성
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findBySlug(String slug);

    List<Category> findByParent_Slug(String parentSlug);  // 상위 slug로 직계 자식 찾기
}
