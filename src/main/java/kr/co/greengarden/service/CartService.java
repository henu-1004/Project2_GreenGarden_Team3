package kr.co.greengarden.service;

import jakarta.persistence.Table;
import kr.co.greengarden.dto.CartDTO;
import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.admin.MemberGeneralListDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Order;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.repository.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.beans.BulkBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void register(CartDTO cartDTO){
        Member member = memberRepository.findById(cartDTO.getMemId()).get();
        Product product = productRepository.findById(cartDTO.getProId()).get();

        List<Cart> carts = cartRepository.findAllByMember_MemId(member.getMemId());
        // 해당 상품이 이미 장바구니에 있는지 확인
        Optional<Cart> existingCart = cartRepository.findByProduct_ProId(cartDTO.getProId());

        String orderNo;

        if (carts.isEmpty()) {
            // 새 주문번호 생성
            orderNo = "B" + System.currentTimeMillis();
        } else {
            // 기존 주문번호 사용
            orderNo = carts.get(0).getOrderNo();
        }

        // Order 생성
        if(!orderRepository.existsById(orderNo)){
            Order order = Order.builder()
                    .member(member)
                    .orderNo(orderNo)
                    .status("결제 대기")
                    .orderedAt(LocalDateTime.now())
                    .build();
            orderRepository.save(order);
        }

        // Cart 생성 또는 업데이트
        if(existingCart.isPresent()){
            Cart oldCart = existingCart.get();

            // 기존 데이터 + 새 수량으로 새 객체 생성
            Cart updatedCart = Cart.builder()
                    .cartId(oldCart.getCartId()) // 기존 ID 유지
                    .member(member)
                    .product(product)
                    .quantity(oldCart.getQuantity() + cartDTO.getQuantity())
                    .orderNo(orderNo)
                    .build();

            cartRepository.save(updatedCart); // ID가 있으므로 UPDATE
        } else {
            // INSERT: 새 장바구니 항목 추가
            Cart cart = Cart.builder()
                    .member(member)
                    .product(product)
                    .quantity(cartDTO.getQuantity())
                    .orderNo(orderNo)
                    .build();
            cartRepository.save(cart);
        }
    }

    public List<CartListDTO> getCartList(int cartId){
        return cartRepository.findCartListByCartId(cartId);
    }

    public List<CartListDTO> getCartListBycartIds(List<Integer> cartIds) {
        return cartRepository.findCartListByCartIds(cartIds);
    }

    public List<CartListDTO> getCartList(String memId){
        return cartRepository.findCartListByMemId(memId);
    }

    public Page<CartListDTO> getCartPage(String memId, int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<CartListDTO> cartList = cartRepository.findCartByMember_MemId(memId, pageable);

        for (CartListDTO c : cartList) {
            int original = (int) Math.ceil(c.getPrice() * (100 - c.getDiscountRate()) / 100.0);
            c.setOriginalPrice(original);
        }

        return cartList;
    }

    public List<Cart> getAllCartsByMemberId(String memId){
        return cartRepository.findAllByMember_MemId(memId);
    }

    public List<Cart> getAllCarts(){
        return cartRepository.findAll();

    }
}
