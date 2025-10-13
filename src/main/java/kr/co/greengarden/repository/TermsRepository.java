package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Integer> {

    Terms findByMemberType(String memberType);
}
