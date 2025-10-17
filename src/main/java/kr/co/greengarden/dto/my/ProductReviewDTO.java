package kr.co.greengarden.dto.my;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Data
public class ProductReviewDTO {
    private Long reviewId;
    private Long proId;
    private String memId;
    private String orderNo;
    private Double rating;
    private String content;
    private LocalDateTime createdAt;
    private String productName; // 상품명


    private String img1;
    private String img2;
    private String img3;

    // 업로드용 파일
    private MultipartFile reviewFile1;
    private MultipartFile reviewFile2;
    private MultipartFile reviewFile3;
}
