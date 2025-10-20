package kr.co.greengarden.dto.admin;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDTO {

    private int deliveryId;
    private String orderNo;
    private String invoiceNo;
    private String status;
    private LocalDateTime createdAt;
    private String note;
}
