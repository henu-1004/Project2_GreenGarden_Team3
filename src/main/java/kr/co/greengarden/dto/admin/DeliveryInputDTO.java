package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryInputDTO {
    private String orderNo;
    private String recName;
    private String recZipCode;
    private String recAddressBasic;
    private String recAddressDetail;
    private String company;
    private String invoiceNo;
    private String Note;
}
