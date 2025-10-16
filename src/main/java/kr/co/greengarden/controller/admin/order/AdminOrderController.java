package kr.co.greengarden.controller.admin.order;


import kr.co.greengarden.dto.OrderDTO;
import kr.co.greengarden.dto.admin.AdminOrderListDTO;
import kr.co.greengarden.service.OrderItemService;
import kr.co.greengarden.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping("/admin/order/list")
    public String orderListPage(Model model) {

        List<AdminOrderListDTO> orderList = orderService.getOrderList();

        model.addAttribute("orderList", orderList);

        return "admin/order/list";
    }

    @GetMapping("/admin/order/delivery")
    public String deliveryPage() {
        return "admin/order/delivery";
    }

    @GetMapping("/admin/order/deliveryDetail")
    public String deliveryDetailPage() {
        return "_deliveryDetail";
    }

    @GetMapping("/admin/order/detail")
    public String detailPage() {
        return "_orderDetail";
    }

}
