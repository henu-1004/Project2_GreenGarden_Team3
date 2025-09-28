package kr.co.greengarden.controller.admin.order;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    @GetMapping("/admin/order/list")
    public String orderListPage() {
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
