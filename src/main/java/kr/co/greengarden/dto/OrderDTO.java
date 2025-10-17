package kr.co.greengarden.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.greengarden.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private String orderNo;

    private Member member; // memId
    private int totalPrice;
    private String payMethod;
    private String status;
    private String recName;
    private String recPhone;
    private String recZipCode;
    private String recAddressBasic;
    private String recAddressDetail;
}
