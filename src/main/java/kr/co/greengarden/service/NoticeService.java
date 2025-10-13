package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Notice;
import kr.co.greengarden.mapper.NoticeMapper;
import kr.co.greengarden.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/02
 * 내용 : 고객센터 - 공지사항 Service 생성
 * */
@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;
    private final NoticeMapper noticeMapper;

    // 공지사항 목록 조회(페이징)
    public PageResponseDTO<NoticeDTO> getNoticesList(PageRequestDTO pageRequestDTO) {
        log.info("pg : {} , type : {} ",pageRequestDTO.getPg(), pageRequestDTO.getType());

        // 1. 목록 데이터 조회

        List<NoticeDTO> dtoList = noticeMapper.selectNoticeList(pageRequestDTO);

        // 2. 전체 개수 조회
        int total = 0;

        return null;
    }

    public Optional<Notice> getNoticeId(Integer noticeId){
        return noticeRepository.findById(noticeId);
    }

    public void save(NoticeDTO NoticeDTO) {
        Notice notice = modelMapper.map(NoticeDTO, Notice.class);
        noticeRepository.save(notice);

    }

    public void delete(Integer noticeId){
        noticeRepository.deleteById(noticeId);
    }
}
