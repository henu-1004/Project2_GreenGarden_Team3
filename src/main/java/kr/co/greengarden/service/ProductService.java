package kr.co.greengarden.service;

import kr.co.greengarden.dto.ProductListDTO;
import kr.co.greengarden.dto.admin.AdminProductListDTO;
import kr.co.greengarden.entity.Category;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.repository.CategoryRepository;
import kr.co.greengarden.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : ProductService 작성
 */
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void register(Product product){
        productRepository.save(product);
    };

    public Optional<Product> getProduct(int proId) {
        return productRepository.findById(proId);
    };

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    };

    public Page<AdminProductListDTO> findProductBySearch(String searchType, String keyword, int page, int size){
        String st = (searchType == null) ? "" : searchType.trim();
        String kw = (keyword == null) ? "" : keyword.trim();
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findProductBySearch(st, kw, pageable);
    }

    public Page<ProductListDTO> getProductCards(int page, String sortBy, String direction, String slug) {

        List<String> slugs = new ArrayList<>();
        slugs.add(slug);
        slugs.addAll(categoryRepository.findByParent_Slug(slug).stream()
                .map(Category::getSlug)
                .toList());

        Set<String> allowed = Set.of("name","price","deliveryFee","discountRate","views","proId","createdAt");
        if (!allowed.contains(sortBy)) sortBy = "proId";  // 기본 정렬 키
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, 10, sort); // 페이지당 10개
        return productRepository.findProducts(pageable, slugs);
    }

    @Transactional
    public void deleteProducts(List<Integer> proIds) {
        productRepository.deleteByProductIdIn(proIds);
    }
}
