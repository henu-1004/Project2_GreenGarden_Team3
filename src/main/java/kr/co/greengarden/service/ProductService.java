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

    public ProductListDTO getProductByProNO(String proNo){
        return productRepository.findProductByProNo(proNo);
    }

    public List<ProductListDTO> getProducts(String sortBy, String direction) {
        Set<String> allowed = Set.of("price", "discountRate", "views", "stock", "proNo", "createdAt");
        if (!allowed.contains(sortBy)) sortBy = "proNo"; // 기본 정렬 키

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // 6개만 가져오도록 제한
        List<ProductListDTO> list = productRepository.findProductOrder(sort);
        return list.size() > 6 ? list.subList(0, 6) : list;
    }

    public List<ProductListDTO> getProductsOrderByRating() {
        List<ProductListDTO> list = productRepository.findProductOrderByRating();
        return list.size() > 6 ? list.subList(0, 5) : list;
    }

    public void register(Product product){
        productRepository.save(product);
    };

    public Product getProduct(int proId) {

        Optional<Product> optProduct = productRepository.findById(proId);
        if (optProduct.isPresent()) {
            return optProduct.get();
        }
        return null;
    }

    public Product getViewProduct(int proId) {
        Optional<Product> optProduct = productRepository.findById(proId);
        if(optProduct.isPresent()){
            Product product = optProduct.get();

            Product updated = product.toBuilder()
                    .price((int) Math.ceil(product.getPrice() * (100 - product.getDiscountRate()) / 100.0))
                    .build();

            return updated;
        }
        return null;
    };

    public Category getCategoryNameBySlug(String slug) {
        Optional<Category> optCategory = categoryRepository.findBySlug(slug);
        if (optCategory.isPresent()) {
            return optCategory.get();
        }
        return null;
    }

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

    @Transactional
    public void updateViewProduct(int proId) {
        productRepository.updateViewByProductId(proId);
    }
}
