package kr.co.greengarden.repository;

import kr.co.greengarden.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/02
 * 내용 : 고객센터 - 공지사항 Repository 생성
 * */
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
