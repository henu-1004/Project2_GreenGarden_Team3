package kr.co.greengarden.service;

import kr.co.greengarden.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : ProductService 작성
 */
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void deleteProducts(List<Integer> proIds) {
        productRepository.deleteByProductIdIn(proIds);
    }
}
