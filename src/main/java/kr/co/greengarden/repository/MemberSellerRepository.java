package kr.co.greengarden.repository;

import kr.co.greengarden.entity.MemberSeller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberSellerRepository 작성
 */
@Repository
public interface MemberSellerRepository extends JpaRepository<MemberSeller, String> {
    @Query(
            value = """
      select s
      from MemberSeller s
      where
        (:keyword is null or :keyword = '') or
        (
          ((:searchType is null or :searchType = '') and (
            lower(s.company)        like lower(concat('%', :keyword, '%')) or
            lower(s.representative) like lower(concat('%', :keyword, '%')) or
            lower(s.tin)            like lower(concat('%', :keyword, '%')) or
            lower(s.tel)            like lower(concat('%', :keyword, '%'))
          ))
          or (:searchType = 'company'         and lower(s.company)        like lower(concat('%', :keyword, '%')))
          or (:searchType = 'representative'  and lower(s.representative) like lower(concat('%', :keyword, '%')))
          or (:searchType = 'tin'             and lower(s.tin)            like lower(concat('%', :keyword, '%')))
          or (:searchType = 'tel'             and lower(s.tel)            like lower(concat('%', :keyword, '%')))
        )
      """,
            countQuery = """
      select count(s)
      from MemberSeller s
      where
        (:keyword is null or :keyword = '') or
        (
          ((:searchType is null or :searchType = '') and (
            lower(s.company)        like lower(concat('%', :keyword, '%')) or
            lower(s.representative) like lower(concat('%', :keyword, '%')) or
            lower(s.tin)            like lower(concat('%', :keyword, '%')) or
            lower(s.tel)            like lower(concat('%', :keyword, '%'))
          ))
          or (:searchType = 'company'         and lower(s.company)        like lower(concat('%', :keyword, '%')))
          or (:searchType = 'representative'  and lower(s.representative) like lower(concat('%', :keyword, '%')))
          or (:searchType = 'tin'             and lower(s.tin)            like lower(concat('%', :keyword, '%')))
          or (:searchType = 'tel'             and lower(s.tel)            like lower(concat('%', :keyword, '%')))
        )
      """
    )
    Page<MemberSeller> findShopBySearch(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable);


}
