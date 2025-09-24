package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberGeneral;
import kr.co.greengarden.repository.MemberGeneralRepository;
import kr.co.greengarden.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PasswordEncoder passwordEncoder;

    // 일반 회원 가입
    @Transactional
    public void save(MemberDTO memberDTO, MemberGeneralDTO generalDTO) {

        // 암호화
        String plain = memberDTO.getPassword();
        String encoded = passwordEncoder.encode(plain);

        memberDTO.setPassword(encoded);

        Member saved = memberRepository.save(memberDTO.toEntity());

        memberGeneralRepository.save(generalDTO.toEntity(saved));
    }

}
