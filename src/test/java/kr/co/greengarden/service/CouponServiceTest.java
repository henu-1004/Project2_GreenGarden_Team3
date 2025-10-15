package kr.co.greengarden.service;

import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void save() {

        // 1. Given (준비): 등록할 DTO 데이터 준비
        CouponDTO inputDto = new CouponDTO();
        inputDto.setType("개별상품할인");
        inputDto.setName("스프링부트 테스트 쿠폰");
        inputDto.setBenefit("10% 할인");
        inputDto.setStartDate(LocalDateTime.now().plusDays(1));
        inputDto.setEndDate(LocalDateTime.now().plusDays(30));

        // 2. When (실행): Service의 등록 메서드 호출
        // CouponDTO resultDto = couponService.save(inputDto);



    }
}