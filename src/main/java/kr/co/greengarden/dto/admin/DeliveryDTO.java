package kr.co.greengarden.dto.admin;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDTO {

    private int deliveryId;
    private String orderNo;
    private String invoiceNo;
    private String status;
    private LocalDate createdAt;
    private String note;
}
