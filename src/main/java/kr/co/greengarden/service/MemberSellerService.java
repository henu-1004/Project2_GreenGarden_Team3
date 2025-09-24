package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.dto.MemberSellerDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.repository.MemberGeneralRepository;
import kr.co.greengarden.repository.MemberRepository;
import kr.co.greengarden.repository.MemberSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberSellerService 작성
 */
@RequiredArgsConstructor
@Service
public class MemberSellerService {
    
    private final MemberRepository memberRepository;
    private final MemberSellerRepository memberSellerRepository;
    private final PasswordEncoder passwordEncoder;

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
}
