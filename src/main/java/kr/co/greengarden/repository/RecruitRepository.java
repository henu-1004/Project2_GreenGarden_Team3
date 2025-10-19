package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Integer> {

}
