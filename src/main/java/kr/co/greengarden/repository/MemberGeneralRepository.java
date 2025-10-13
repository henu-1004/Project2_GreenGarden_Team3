package kr.co.greengarden.repository;

import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.admin.MemberGeneralListDTO;
import kr.co.greengarden.dto.admin.MemberGerneralModifyDTO;
import kr.co.greengarden.entity.MemberGeneral;
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
 * 내용 : MemberGeneralRepository 작성
 */
@Repository
public interface MemberGeneralRepository extends JpaRepository<MemberGeneral, String> {

    @Query("""
        select new kr.co.greengarden.dto.admin.MemberGerneralModifyDTO(
          m.memId, g.name, g.gender, gr.grade, g.status,
          g.email, g.phone, m.zipCode, m.addressBasic, m.addressDetail,
          m.joinDate, g.lastLogin, g.note
        )
        from Member m
        left join m.general g
        left join m.grade gr
        where m.role = 'GENERAL' and m.memId = :memId
    """)
    MemberGerneralModifyDTO findGeneralById(@Param("memId") String memId);

    @Query(
            value = """
            select new kr.co.greengarden.dto.admin.MemberGeneralListDTO(
                m.memId,
                g.name,
                g.gender,
                g.name,
                p.totalPoint,
                g.email,
                g.phone,
                m.joinDate,
                g.status
            )
            from Member m
            left join m.general g
            left join m.point p
            left join m.grade gr
            where m.role = 'GENERAL'
              and (
                :keyword is null or :keyword = '' or
                (
                  (:searchType is null or :searchType = '') and (
                    lower(m.memId) like lower(concat('%', :keyword, '%')) or
                    lower(g.name)   like lower(concat('%', :keyword, '%')) or
                    lower(g.email)  like lower(concat('%', :keyword, '%')) or
                    lower(g.phone)  like lower(concat('%', :keyword, '%'))
                  )
                ) or
                (:searchType = 'userId' and lower(m.memId) like lower(concat('%', :keyword, '%'))) or
                (:searchType = 'name'   and lower(g.name)  like lower(concat('%', :keyword, '%'))) or
                (:searchType = 'email'  and lower(g.email) like lower(concat('%', :keyword, '%'))) or
                (:searchType = 'phone'  and lower(g.phone) like lower(concat('%', :keyword, '%')))
              )
            order by m.joinDate desc
            """,
            countQuery = """
            select count(m)
            from Member m
            left join m.general g
            where m.role = 'GENERAL'
              and (
                :keyword is null or :keyword = '' or
                (
                  (:searchType is null or :searchType = '') and (
                    lower(m.memId) like lower(concat('%', :keyword, '%')) or
                    lower(g.name)   like lower(concat('%', :keyword, '%')) or
                    lower(g.email)  like lower(concat('%', :keyword, '%')) or
                    lower(g.phone)  like lower(concat('%', :keyword, '%'))
                  )
                ) or
                (:searchType = 'userId' and lower(m.memId) like lower(concat('%', :keyword, '%'))) or
                (:searchType = 'name'   and lower(g.name)  like lower(concat('%', :keyword, '%'))) or
                (:searchType = 'email'  and lower(g.email) like lower(concat('%', :keyword, '%'))) or
                (:searchType = 'phone'  and lower(g.phone) like lower(concat('%', :keyword, '%')))
              )
            """
    )
    Page<MemberGeneralListDTO> findGeneralBySearch(@Param("searchType") String searchType,
                                                       @Param("keyword") String keyword,
                                                       Pageable pageable);

    @Modifying
    @Query("delete from MemberGeneral g where g.memId in :ids")
    void deleteByMemIdIn(@Param("ids") List<String> ids);

}
