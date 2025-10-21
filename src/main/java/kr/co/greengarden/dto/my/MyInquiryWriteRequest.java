package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyInquiryWriteRequest {

    private String category;
    private String detailCategory;
    private String title;
    private String content;
    private String sellerId;
    private String orderNo;
}
