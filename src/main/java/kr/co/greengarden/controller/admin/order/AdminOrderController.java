package kr.co.greengarden.controller.admin.order;


import kr.co.greengarden.dto.admin.DeliveryDTO;
import kr.co.greengarden.dto.admin.AdminOrderListDTO;
import kr.co.greengarden.service.OrderItemService;
import kr.co.greengarden.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping("/admin/order/list")
    public String orderListPage(@RequestParam(required = false) String searchType,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {

        Page<AdminOrderListDTO> orderList = orderService.findAllOrderBySearch(searchType, keyword, page, 5);
        model.addAttribute("orderList", orderList);

        return "admin/order/list";
    }

    @GetMapping("/admin/order/delivery")
    public String deliveryPage(@RequestParam(required = false) String searchType,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {

        Page<DeliveryDTO> deliveryList = orderService.findAllDeliveryBySearch(searchType, keyword, page, 5);
        model.addAttribute("deliveryList", deliveryList);

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
