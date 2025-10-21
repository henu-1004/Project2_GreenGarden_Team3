package kr.co.greengarden.controller.my;

import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.my.MyInquiryWriteRequest;
import kr.co.greengarden.dto.my.OrderDetailDTO;
import kr.co.greengarden.dto.my.SellerDetailDTO;
import kr.co.greengarden.service.InquiryService;
import kr.co.greengarden.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/my/api")
@RequiredArgsConstructor
public class MyOrderRestController {

    private final MyService myService;
    private final InquiryService inquiryService;

    @GetMapping("/orders/{orderNo}")
    public ResponseEntity<OrderDetailDTO> getOrderDetail(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable String orderNo) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        OrderDetailDTO detail = myService.getOrderDetail(userDetails.getUsername(), orderNo);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<SellerDetailDTO> getSeller(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable String sellerId) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SellerDetailDTO seller = myService.getSellerDetail(sellerId);
        if (seller == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seller);
    }

    @PostMapping("/inquiry")
    public ResponseEntity<Map<String, Object>> submitInquiry(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestBody MyInquiryWriteRequest request) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (request == null || isBlank(request.getTitle()) || isBlank(request.getContent())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "문의 제목과 내용을 입력해주세요."));
        }

        String category1 = isBlank(request.getCategory()) ? "기타" : request.getCategory();
        String category2 = isBlank(request.getDetailCategory()) ? null : request.getDetailCategory();

        if (!isBlank(request.getSellerId())) {
            category2 = appendMeta(category2, "판매자:" + request.getSellerId());
        }
        if (!isBlank(request.getOrderNo())) {
            category2 = appendMeta(category2, "주문번호:" + request.getOrderNo());
        }

        InquiryDTO inquiryDTO = InquiryDTO.builder()
                .category1(category1)
                .category2(category2)
                .title(request.getTitle().trim())
                .content(request.getContent().trim())
                .channel("MY_PAGE")
                .build();

        int inquiryId = inquiryService.registerInquiry(inquiryDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("inquiryId", inquiryId);
        response.put("message", "문의가 등록되었습니다.");
        return ResponseEntity.ok(response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String appendMeta(String base, String addition) {
        if (base == null || base.isBlank()) {
            return addition;
        }
        return base + " | " + addition;
    }
}
