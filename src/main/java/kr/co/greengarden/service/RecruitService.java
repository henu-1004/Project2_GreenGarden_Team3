package kr.co.greengarden.service;

import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.dto.RecruitDTO;
import kr.co.greengarden.entity.Recruit;
import kr.co.greengarden.mapper.RecruitMapper;
import kr.co.greengarden.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/*
 * 이름 : 박효빈
 * 날짜 : 2025/10/19
 * 내용 : 채용공고 서비스 (목록 MyBatis, 등록 JPA)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitMapper recruitMapper;         // MyBatis (목록, 검색, 페이징)
    private final RecruitRepository recruitRepository; // JPA (등록, 수정, 삭제)

    /**
     * 채용 목록 조회 (페이징 + 검색)
     */
    public PageResponseDTO<RecruitDTO> getRecruitList(PageRequestDTO pageRequestDTO) {
        log.info("채용 목록 요청: {}", pageRequestDTO);

        List<RecruitDTO> dtoList = recruitMapper.selectRecruitList(pageRequestDTO);
        int total = recruitMapper.selectRecruitCount(pageRequestDTO);

        log.info("채용공고 목록 조회 완료 - {}건 / 총 {}", dtoList.size(), total);

        return PageResponseDTO.<RecruitDTO>builder()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    /**
     * 단일 채용 공고 조회
     */
    public RecruitDTO getRecruit(Integer recruitId) {
        return recruitMapper.selectRecruit(recruitId);
    }

    /**
     * 최신 채용공고 n개 조회 (홈, 관리자 대시보드용)
     */
    public List<RecruitDTO> getLatestRecruit(int limit) {
        return recruitMapper.selectLatestRecruit(limit);
    }

    /**
     * 채용공고 등록 (JPA)
     */
    @Transactional
    public int registerRecruit(RecruitDTO dto) {
        Recruit recruit = Recruit.builder()
                .department(dto.getDepartment())
                .career(dto.getCareer())
                .type(dto.getType())
                .title(dto.getTitle())
                .writer("관리자") // 로그인 정보에서 추후 대체
                .status("모집중")
                .period(dto.getPeriod())
                .note(dto.getNote())
                .createdAt(LocalDateTime.now())
                .build();

        Recruit saved = recruitRepository.save(recruit);
        log.info("✅ 채용 등록 완료 ID={}", saved.getRecruitId());

        return saved.getRecruitId();
    }

    /**
     * 채용공고 삭제 (JPA)
     */
    @Transactional
    public void deleteRecruit(Integer recruitId) {
        recruitRepository.deleteById(recruitId);
        log.info("✅ 채용 삭제 완료 ID={}", recruitId);
    }

    /**
     * 채용 상태 변경 (예: 모집중 → 종료)
     */
    @Transactional
    public void closeRecruit(Integer recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new RuntimeException("채용공고를 찾을 수 없습니다."));

        recruit.closeRecruit(); // 엔티티 도메인 메서드 호출
        log.info("채용 상태 변경 완료: ID={} → 상태={}", recruitId, recruit.getStatus());
    }
}
