package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryListDTO {
    private String invoiceNo;
    private String company;
    private String orderNo;
    private String recName;
    private String name;
    private Long quantity;
    private Integer totalPrice;    // int → Integer로 변경
    private Long deliveryFee;      // 이미 Long이므로 OK
    private String status;
    private LocalDateTime createdAt;
}
