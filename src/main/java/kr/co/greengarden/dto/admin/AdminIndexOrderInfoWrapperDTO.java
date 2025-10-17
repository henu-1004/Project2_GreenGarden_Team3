package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminIndexOrderInfoWrapperDTO {
    private int statusCount;
    private int totalPrice;
    private int count;
}
