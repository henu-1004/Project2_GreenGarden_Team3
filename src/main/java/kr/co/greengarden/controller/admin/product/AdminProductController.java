package kr.co.greengarden.controller.admin.product;

import kr.co.greengarden.dto.ProductDTO;
import kr.co.greengarden.dto.admin.AdminProductListDTO;
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

import java.util.List;
import java.util.Optional;

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
        return "/admin/product/list";
    }

    @GetMapping("admin/product/register")
    public String registerForm() {
        return "/admin/product/register";
    }

    @PostMapping("/admin/product/register")
    public String register(@AuthenticationPrincipal MemberDetails memberDetails,
                           @ModelAttribute ProductDTO productDTO,
                           @RequestParam(required = false) MultipartFile imgFile1,
                           @RequestParam(required = false) MultipartFile imgFile2,
                           @RequestParam(required = false) MultipartFile imgFile3,
                           @RequestParam(required = false) MultipartFile imgFile_detail,
                           @RequestParam(required = false) String classification1,
                           @RequestParam(required = false) String classification2) {

        try {
            productDTO.setImg1(imageHandler.saveImage(imgFile1, "product"));
            productDTO.setImg2(imageHandler.saveImage(imgFile2, "product"));
            productDTO.setImg3(imageHandler.saveImage(imgFile3, "product"));
            productDTO.setImgDetail(imageHandler.saveImage(imgFile_detail, "product"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String memId = memberDetails.getUsername();
        Optional<MemberSeller> optionalMember = memberSellerService.getUser(memId);
        MemberSeller seller = memberSellerService.getUser(memId)
                .orElseThrow(() -> new IllegalStateException("판매자 계정을 찾을 수 없습니다."));

        String slug = productDTO.getCategorySlug();
        if (slug == null || slug.isBlank()) {
            slug = (classification2 != null && !classification2.isBlank()
                    && !"null".equalsIgnoreCase(classification2)
                    && !"undefined".equalsIgnoreCase(classification2))
                    ? classification2
                    : classification1;
        }
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("카테고리(slug)가 지정되지 않았습니다.");
        }

        Category category = categoryService.getCategoryBySlug(slug);

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