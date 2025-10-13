package kr.co.greengarden.mapper;

import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/13
 * 내용 : 고객센터 - 공지사항 Mapper 인터페이스
 *        NoticeMapper.xml의 SQL 쿼리와 연결되어
 *        공지사항 목록, 상세보기, 조회수 증가 등의 DB 처리를 담당함
 */

@Mapper
public interface NoticeMapper {

    // List 먼저 구현 하기위해 selectNoticeList와 매핑
    List<NoticeDTO> selectNoticeList(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 전체 개수 (Total)
    int selectNoticeCount(@Param("pageRequestDTO")  PageRequestDTO pageRequestDTO);

    // 상세보기 (View)
    NoticeDTO selectNotice(@Param("noticeId") Integer noticeId);

    // 조회수 증가
    void updateViews(@Param("noticeId") Integer noticeId);
}
