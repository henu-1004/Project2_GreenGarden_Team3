package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.MemberSellerDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.repository.*;
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
 * 내용 : MemberSellerService 작성
 */
@RequiredArgsConstructor
@Service
public class MemberSellerService {

    private final PointRepository pointRepository;
    private final GradeRepository gradeRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberRepository memberRepository;
    private final MemberSellerRepository memberSellerRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<MemberSeller> getUser(String memId){
        return memberSellerRepository.findById(memId);
    }

    public Page<MemberSeller> findShopBySearch(String searchType, String keyword, int page, int size){
        String st = (searchType == null) ? "" : searchType.trim();
        String kw = (keyword == null) ? "" : keyword.trim();
        Pageable pageable = PageRequest.of(page, size);
        return memberSellerRepository.findShopBySearch(st, kw, pageable);
    }

    // 판매 회원 가입
    @Transactional
    public void save(MemberDTO memberDTO, MemberSellerDTO sellerDTO) {

        // 암호화
        String plain = memberDTO.getPassword();
        String encoded = passwordEncoder.encode(plain);

        memberDTO.setPassword(encoded);

        Member saved = memberRepository.save(memberDTO.toEntity());

        memberSellerRepository.save(sellerDTO.toEntity(saved));
    }

    @Transactional
    public void deleteShops(List<String> memIds) {
        // 자식 먼저
        pointRepository.deleteByMemberIdIn(memIds);
        gradeRepository.deleteByMemIdIn(memIds);
        memberGeneralRepository.deleteByMemIdIn(memIds);
        // 부모
        memberRepository.deleteAllByIdInBatch(memIds);
    }
}
