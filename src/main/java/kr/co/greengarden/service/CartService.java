package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.repository.CartRepository;
import kr.co.greengarden.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : CartService 작성
 */
@RequiredArgsConstructor
@Service
public class CartService {
    
    private final CartRepository cartRepository;

}
