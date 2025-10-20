package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Integer> {

    Terms findByMemberType(String memberType);

    Terms findFirstByTermsFinIsNotNullOrderByTermsIdDesc();
    Terms findFirstByTermsLocIsNotNullOrderByTermsIdDesc();
    Terms findFirstByTermsPrivIsNotNullOrderByTermsIdDesc();


    @Modifying(clearAutomatically = true,  flushAutomatically = true)
    @Transactional
    @Query("update Terms t set t.termsUse = :content where t.memberType = :memberType")
    int updateTermsUseByMemberType(@Param("memberType") String memberType,
                                   @Param("content") String content);

    @Modifying(clearAutomatically = true,  flushAutomatically = true)
    @Transactional
    @Query("update Terms t set t.termsFin = :content where t.memberType in('USER', 'SELLER')")
    int updateTermsFinForAll(@Param("content") String content);

    @Modifying(clearAutomatically = true,  flushAutomatically = true)
    @Transactional
    @Query("update Terms t set t.termsLoc = :content where t.memberType in('USER', 'SELLER')")
    int updateTermsLocForAll(@Param("content") String content);

    @Modifying(clearAutomatically = true,  flushAutomatically = true)
    @Transactional
    @Query("update Terms t set t.termsPriv = :content where t.memberType in('USER', 'SELLER')")
    int  updateTermsPrivForAll(@Param("content") String content);
}
