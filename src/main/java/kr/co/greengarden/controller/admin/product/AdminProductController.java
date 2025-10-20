package kr.co.greengarden.controller.admin.product;

import kr.co.greengarden.dto.ProductDTO;
import kr.co.greengarden.dto.admin.AdminProductListDTO;
import kr.co.greengarden.dto.admin.CategorySlugDTO;
import kr.co.greengarden.entity.Category;
import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.handler.ImageHandler;
import kr.co.greengarden.security.MemberDetails;
import kr.co.greengarden.service.CategoryService;
import kr.co.greengarden.service.MemberSellerService;
import kr.co.greengarden.service.MemberService;
import kr.co.greengarden.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 날짜 : 2025/10/20
 * 이름 : 박효빈
 * 내용 : ProductController ProNo 자동 생성 기능 구현
 * */
@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final MemberSellerService memberSellerService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final ImageHandler imageHandler;

    @GetMapping("/admin/product/list")
    public String shopList(@RequestParam(required = false) String searchType,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {

        Page<AdminProductListDTO> productList = productService.findProductBySearch(searchType, keyword, page, 5);
        model.addAttribute("productList", productList);
        return "admin/product/list";
    }

    @GetMapping("/admin/product/register")
    public String registerForm(Model model) {
        List<CategorySlugDTO> parentSlugList = categoryService.getCategoryParentSlug();

        model.addAttribute("parentSlugList", parentSlugList);

        return "admin/product/register";
    }

    @GetMapping("/admin/product/register/category/{parentId}")
    @ResponseBody
    public List<CategorySlugDTO> children(@PathVariable int parentId) {
        return categoryService.getCategoryChildrenSlug(parentId);
    }

    /** ✅ 상품 등록 처리 */
    @PostMapping("/admin/product/register")
    public String register(@AuthenticationPrincipal MemberDetails memberDetails,
                           @ModelAttribute ProductDTO productDTO,
                           @RequestParam(required = false) MultipartFile imgFile1,
                           @RequestParam(required = false) MultipartFile imgFile2,
                           @RequestParam(required = false) MultipartFile imgFile3,
                           @RequestParam(required = false) MultipartFile imgFile_detail,
                           @RequestParam(required = false) String slug) {

        try {
            productDTO.setImg1(imageHandler.saveImage(imgFile1, "product"));
            productDTO.setImg2(imageHandler.saveImage(imgFile2, "product"));
            productDTO.setImg3(imageHandler.saveImage(imgFile3, "product"));
            productDTO.setImgDetail(imageHandler.saveImage(imgFile_detail, "product"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // ✅ 로그인한 관리자 정보 (없으면 임시 "admin_test"로 설정)
        String memId = (memberDetails != null) ? memberDetails.getUsername() : "admin_test";

        // ✅ 날짜 + 순번 상품코드 자동 생성 (예: P202510200001)
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = productService.getAllProducts().size() + 1; // 총 상품 수 기반
        String productNo = String.format("P%s%04d", today, count);
        productDTO.setProNo(productNo);

        // ✅ 판매자, 카테고리 정보 세팅
        Category category = categoryService.getCategoryBySlug(slug);
        MemberSeller seller = memberSellerService.getUser(memId)
                .orElseThrow(() -> new IllegalStateException("판매자 계정을 찾을 수 없습니다."));

        // ✅ 저장
        productService.register(productDTO.toEntity(seller, category));

        return "redirect:/admin/product/list";
    }


    @PostMapping("/admin/product/deleteSelected")
    public String deleteSelected(@RequestParam("proIds") List<Integer> proIds,
                                 RedirectAttributes ra) {
        if (proIds == null || proIds.isEmpty()) {
            ra.addFlashAttribute("msg", "선택된 항목이 없습니다.");
            return "redirect:/admin/product/list";
        }

        productService.deleteProducts(proIds);
        ra.addFlashAttribute("msg", proIds.size() + "건 삭제되었습니다.");
        return "redirect:/admin/product/list";
    }
}