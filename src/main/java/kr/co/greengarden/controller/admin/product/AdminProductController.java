package kr.co.greengarden.controller.admin.product;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;
import kr.co.greengarden.dto.ProductDTO;
import kr.co.greengarden.dto.admin.ProductListDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.entity.Product;
import kr.co.greengarden.handler.ImageHandler;
import kr.co.greengarden.security.MemberDetails;
import kr.co.greengarden.service.MemberSellerService;
import kr.co.greengarden.service.MemberService;
import kr.co.greengarden.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final MemberService memberService;
    private final ImageHandler imageHandler;

    @GetMapping("/admin/product/list")
    public String shopList(@RequestParam(required = false) String searchType,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {

        Page<ProductListDTO> productList = productService.findProductBySearch(searchType, keyword, page, 5);
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
                           @RequestParam(required = false) MultipartFile imgFile_detail) {

        try {
            productDTO.setImg1(imageHandler.saveImage(imgFile1, "product"));
            productDTO.setImg2(imageHandler.saveImage(imgFile2, "product"));
            productDTO.setImg3(imageHandler.saveImage(imgFile3, "product"));
            productDTO.setImgDetail(imageHandler.saveImage(imgFile_detail, "product"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String memId = memberDetails.getUsername();

        System.out.println("로그인된 아이디 : " + memId);

        Optional<MemberSeller> optionalMember = memberSellerService.getUser(memId);

        System.out.println("유저 : " + optionalMember.get());
        MemberSeller seller = optionalMember.get();

        productService.register(productDTO.toEntity(seller));
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