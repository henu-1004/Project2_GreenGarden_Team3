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
import org.springframework.transaction.annotation.Transactional;

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
        int total = noticeMapper.selectNoticeCount(pageRequestDTO);

        log.info("조회 된 공지사항 {} 개, 전체 {} 개",dtoList.size(), total);

        // 3. PageResponseDTO 생성 (자동 페이징 계산)


        return PageResponseDTO.<NoticeDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    /*
    * 공지사항 상세보기 ( 조회수 증가 )
    * */
    @Transactional
    public NoticeDTO getNotice(int noticeId) {
        log.info("getNotice : {}", noticeId);

        // 조회수 증가 (트랜잭션으로 묶어버림)
        noticeMapper.updateViews(noticeId);
        log.info("getNotice : {}", noticeId);

        // 상세 정보 조회 ( 내용 포함 전체 데이터 )
        NoticeDTO noticeDTO = noticeMapper.selectNotice(noticeId);

        if (noticeDTO == null) {
            log.info("getNotice : {}", noticeId);
            return null;
        }
        log.info("getNotice : {}", noticeId);


        return noticeDTO;
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
