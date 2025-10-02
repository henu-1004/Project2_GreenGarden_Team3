package kr.co.greengarden.service;

import kr.co.greengarden.dto.CartDTO;
import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Category;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.repository.CartRepository;
import kr.co.greengarden.repository.CategoryRepository;
import kr.co.greengarden.repository.MemberRepository;
import kr.co.greengarden.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public Category getCategoryBySlug(String slug) {
        Optional<Category> optCategory = categoryRepository.findBySlug(slug);

        if (optCategory.isPresent()) {
            return optCategory.get();
        }
        return null;
    }

    public List<Category> getCategoriesByParent(String parentSlug) {
        return categoryRepository.findByParent_Slug(parentSlug);
    }
}
