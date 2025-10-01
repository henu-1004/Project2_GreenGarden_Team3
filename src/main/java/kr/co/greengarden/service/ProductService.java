package kr.co.greengarden.service;

import kr.co.greengarden.dto.admin.ProductListDTO;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : ProductService 작성
 */
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public void register(Product product){
        productRepository.save(product);
    };

    public Optional<Product> getProduct(int proId) {
        return productRepository.findById(proId);
    };

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    };

    public Page<ProductListDTO> findProductBySearch(String searchType, String keyword, int page, int size){
        String st = (searchType == null) ? "" : searchType.trim();
        String kw = (keyword == null) ? "" : keyword.trim();
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findProductBySearch(st, kw, pageable);
    }

    @Transactional
    public void deleteProducts(List<Integer> proIds) {
        productRepository.deleteByProductIdIn(proIds);
    }
}
