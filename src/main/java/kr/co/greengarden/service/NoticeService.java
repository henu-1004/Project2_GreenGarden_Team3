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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // null 체크
        if (dtoList == null) {
            dtoList = List.of(); // 빈 리스트로 초기화
        }

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
    // 조회수 증가 없이 조회 ( 수정 폼용)
    public NoticeDTO getNoticeById(Integer noticeId){
        NoticeDTO noticeDTO = noticeMapper.selectNotice(noticeId);
        return noticeDTO;
    }

    // 수정 (POST)
    @Transactional
    public void modifyNotice(NoticeDTO noticeDTO) {
        Notice notice = noticeRepository.findById(noticeDTO.getNoticeId()).orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다"));

        notice.update(
                noticeDTO.getType(),
                noticeDTO.getTitle(),
                noticeDTO.getContent()
        );
    }

    // 공지사항 작성
    @Transactional
    public int registerNotice(NoticeDTO NoticeDTO) {
        // 1. 작성자 정보 설정
        String witerId = getLoggedInUserId(); // 아레 코드에서 가져오기

        // 2. DTO -> Entity  변환
        Notice notice = modelMapper.map(NoticeDTO, Notice.class);

        // 3. DB 저장
        Notice savedNotice = noticeRepository.save(notice);

        log.info("공지사항 등록 완료 ID : {}", savedNotice.getNoticeId()) ;
        return savedNotice.getNoticeId();
    }

    public void delete(Integer noticeId){
        noticeRepository.deleteById(noticeId);
    }

    //공지사항 5개 가져오는 서비스 ? 만들기
    public List<NoticeDTO> getLatestNotices(int limit) {
        // 컨트롤러에서 넘어온 limit(5) 그대로 Mapper에 전달
        return noticeMapper.selectLatestNotices(limit);
    }

    // 아이디 호출
    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "관리자";
        }
        return auth.getName();
    }
}
