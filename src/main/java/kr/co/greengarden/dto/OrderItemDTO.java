package kr.co.greengarden.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {

    private int orderItemId;
    private String orderNo;
    private int cartId;
    private int quantity;
    private int price;
    private int discountRate;
    private int pointUsed;
    private int proId;
}
