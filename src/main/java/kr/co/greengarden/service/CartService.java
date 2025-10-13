package kr.co.greengarden.service;

import kr.co.greengarden.dto.CartDTO;
import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.repository.CartRepository;
import kr.co.greengarden.repository.MemberRepository;
import kr.co.greengarden.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public void register(CartDTO cartDTO){
        Member member = memberRepository.findById(cartDTO.getMemId()).get();
        Product product = productRepository.findById(cartDTO.getProId()).get();
        Cart cart = cartDTO.toEntity(member, product);
        cartRepository.save(cart);
    }

    public List<CartListDTO> getCartList(String memId){
        return cartRepository.findCartListByMemId(memId);
    }

    public List<Cart> getAllCartsByMemberId(String memId){
        return cartRepository.findAllByMember_MemId(memId);
    }

    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }

}
