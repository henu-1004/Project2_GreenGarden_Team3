package kr.co.greengarden.controller.admin.order;


import kr.co.greengarden.dto.admin.*;
import kr.co.greengarden.entity.Delivery;
import kr.co.greengarden.service.DeliveryService;
import kr.co.greengarden.service.OrderItemService;
import kr.co.greengarden.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final DeliveryService deliveryService;
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

    @GetMapping("/admin/order/orderDetail/{orderNo}")
    @ResponseBody
    public List<AdminOrderDetailListDTO> getOrderDetail(@PathVariable String orderNo) {
        return orderService.findOrderDetailList(orderNo);
    }

    @GetMapping("/admin/order/deliveryDetail/{orderNo}")
    @ResponseBody
    public List<AdminDeliveryDetailListDTO> getDeliveryDetail(@PathVariable String orderNo) {
        System.out.println("상세 요청 invoiceNo: " + orderNo);
        return orderService.findDeliveryDetailList(orderNo);
    }

    @GetMapping("/admin/order/deliveryInput/{orderNo}")
    @ResponseBody
    public DeliveryInputDTO deliveryInput(@PathVariable String orderNo) {
        return orderService.findDeliveryInfo(orderNo);
    }

    @PostMapping("/admin/order/deliveryRegister")
    public String deliveryRegister(DeliveryDTO deliveryDTO) {

        deliveryService.register(deliveryDTO);

        return "redirect:/admin/order/delivery";
    }


    @GetMapping("/admin/order/delivery")
    public String deliveryPage(@RequestParam(required = false) String searchType,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {

        Page<DeliveryListDTO> deliveryList = orderService.findAllDeliveryBySearch(searchType, keyword, page, 5);
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
