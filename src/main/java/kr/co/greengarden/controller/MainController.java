package kr.co.greengarden.controller;


import kr.co.greengarden.controller.product.ProductController;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.ProductListDTO;
import kr.co.greengarden.dto.admin.AdminProductListDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.security.MemberDetails;
import kr.co.greengarden.service.FaqService;
import kr.co.greengarden.service.InquiryService;
import kr.co.greengarden.service.NoticeService;
import kr.co.greengarden.service.ProductService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MainController 설정
 */
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final NoticeService noticeService;
    private final FaqService faqService;
    private final InquiryService inquiryService;

    @GetMapping(value = {"/", "/index"})
    public String index(Authentication authentication, Model model) {

        List<ProductListDTO> hitList = productService.getProducts("views", "desc");
        List<ProductListDTO> newList = productService.getProducts("createdAt", "desc");

        if(authentication != null) {

            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
            Member member = memberDetails.getMember();

            model.addAttribute("member", member);
        }

        model.addAttribute("newList", newList);
        model.addAttribute("hitList", hitList);

        return "index";
    }

    /*
     * 날짜 : 2025/09/26
     * 이름 : 박효빈
     * 내용 : MainController + cs index page 추가 연결
     */
    @GetMapping(value = {"/cs"})
    public String csindexpage(Model model) {

        // 공지사항 5개 로드 (최신)
        model.addAttribute("latestNotices", noticeService.getLatestNotices(5));

        //model.addAttribute("faqCategories", List.of()); // 빈 리스트 임시 전달
        model.addAttribute("latestInquiries", inquiryService.getLatestInquiry(5)); // 빈 리스트 임시 전달
        return "cs/cs_index";
    }
}