package kr.co.greengarden.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemListWrapper {
    private List<OrderItemDTO> items;
}
