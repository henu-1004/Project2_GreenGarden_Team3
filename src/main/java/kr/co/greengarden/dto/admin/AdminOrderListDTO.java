package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminOrderListDTO {
    private String orderNo;
    private String memId;
    private String name;
    private int quantity;  // null 가능하도록 Integer 사용
    private int totalPrice;
    private String payMethod;
    private String status;
    private LocalDateTime orderedAt;
}
