package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 판매자 정보 모달 데이터 전송 객체.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerInfoDTO {

    private String sellerId;       // 판매자 회원 ID
    private String gradeName;      // 판매자 등급
    private String company;        // 상호명
    private String representative; // 대표자명
    private String tel;            // 연락처
    private String fax;            // 팩스 번호
    private String email;          // 이메일
    private String businessNumber; // 사업자등록번호
    private String zipCode;        // 우편번호
    private String addressBasic;   // 기본 주소
    private String addressDetail;  // 상세 주소

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (zipCode != null && !zipCode.isBlank()) {
            sb.append('[').append(zipCode).append("] ");
        }
        if (addressBasic != null) {
            sb.append(addressBasic);
        }
        if (addressDetail != null && !addressDetail.isBlank()) {
            sb.append(' ').append(addressDetail);
        }
        return sb.toString().trim();
    }
}

