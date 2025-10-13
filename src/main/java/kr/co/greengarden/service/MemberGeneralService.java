package kr.co.greengarden.service;

import kr.co.greengarden.dto.GradeDTO;
import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.PointDTO;
import kr.co.greengarden.dto.admin.MemberGeneralListDTO;
import kr.co.greengarden.dto.admin.MemberGerneralModifyDTO;
import kr.co.greengarden.entity.Grade;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberGeneral;
import kr.co.greengarden.repository.GradeRepository;
import kr.co.greengarden.repository.MemberGeneralRepository;
import kr.co.greengarden.repository.MemberRepository;
import kr.co.greengarden.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberGeneralService 작성
 */
@RequiredArgsConstructor
@Service
public class MemberGeneralService {

    private final MemberRepository memberRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final GradeRepository gradeRepository;
    private final PointRepository pointRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 찾기
    public Optional<MemberGeneral> getUser(String memId){
        return memberGeneralRepository.findById(memId);
    }

    // 검색
    public Page<MemberGeneralListDTO> findGeneralBySearch(String searchType, String keyword, int page, int size){
        String st = (searchType == null) ? "" : searchType.trim();
        String kw = (keyword == null) ? "" : keyword.trim();
        Pageable pageable = PageRequest.of(page, size);
        return memberGeneralRepository.findGeneralBySearch(st, kw, pageable);
    }

    // 수정 데이터
    public MemberGerneralModifyDTO findGeneralById(String memId){
        return memberGeneralRepository.findGeneralById(memId);
    }

    // 일반 회원 가입
    @Transactional
    public void save(MemberDTO memberDTO, MemberGeneralDTO generalDTO) {

        // 암호화
        String plain = memberDTO.getPassword();
        String encoded = passwordEncoder.encode(plain);

        memberDTO.setPassword(encoded);

        Member saved = memberRepository.save(memberDTO.toEntity());

        memberGeneralRepository.save(generalDTO.toEntity(saved));

        GradeDTO gradeDTO = GradeDTO.builder()
                                    .memId(saved.getMemId())
                                    .build();
        gradeRepository.save(gradeDTO.toEntity(saved));

        PointDTO pointDTO = PointDTO.builder()
                                    .memId(saved.getMemId())
                                    .build();
        pointRepository.save(pointDTO.toEntity(saved));
    }

    //수정
    @Transactional
    public void modifyGeneral(Member member, MemberGeneral general, Grade grade) {
        memberRepository.save(member);
        memberGeneralRepository.save(general);
        gradeRepository.save(grade);
    }

    @Transactional
    public void deleteMembers(List<String> memIds) {
        // 자식 먼저
        pointRepository.deleteByMemberIdIn(memIds);
        gradeRepository.deleteByMemIdIn(memIds);
        memberGeneralRepository.deleteByMemIdIn(memIds);
        // 부모
        memberRepository.deleteAllByIdInBatch(memIds);
    }



}
