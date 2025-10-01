package kr.co.greengarden.repository;

import kr.co.greengarden.dto.admin.ProductListDTO;
import kr.co.greengarden.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(
            value = """
    select new kr.co.greengarden.dto.admin.ProductListDTO(
       p.proId, p.img1, p.proNo, p.name, p.price, p.discountRate, p.point, p.stock, s.company, p.views
    )
    from Product p
    join p.seller s
    where
      (:keyword is null or :keyword = '')
      or (
        ( :searchType is null or :searchType = '' ) and (
          lower(p.name)    like lower(concat('%', :keyword, '%')) or
          lower(p.proNo)  like lower(concat('%', :keyword, '%')) or
          lower(s.company) like lower(concat('%', :keyword, '%'))
        )
      )
      or (:searchType = 'name'    and lower(p.name)    like lower(concat('%', :keyword, '%')))
      or (:searchType = 'proNo'  and lower(p.proNo)  like lower(concat('%', :keyword, '%')))
      or (:searchType = 'company' and lower(s.company) like lower(concat('%', :keyword, '%')))
    order by p.proNo desc
  """,
            countQuery = """
    select count(p)
    from Product p
    join p.seller s
    where
      (:keyword is null or :keyword = '')
      or (
        ( :searchType is null or :searchType = '' ) and (
          lower(p.name)    like lower(concat('%', :keyword, '%')) or
          lower(p.proNo)  like lower(concat('%', :keyword, '%')) or
          lower(s.company) like lower(concat('%', :keyword, '%'))
        )
      )
      or (:searchType = 'name'    and lower(p.name)    like lower(concat('%', :keyword, '%')))
      or (:searchType = 'proNo'  and lower(p.proNo)  like lower(concat('%', :keyword, '%')))
      or (:searchType = 'company' and lower(s.company) like lower(concat('%', :keyword, '%')))
  """
    )
    Page<ProductListDTO> findProductBySearch(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Modifying
    @Query("delete from Product p where p.proId in :ids")
    void deleteByProductIdIn(@Param("ids") List<Integer> ids);
}
