package kr.co.greengarden.controller.my;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/my")
public class MyController {

    @GetMapping("my/home")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/home"; // templates/my/home.html
    }

    @GetMapping("my/order")
    public String order(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/order"; // templates/my/order.html
    }

    @GetMapping("my/point")
    public String point(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/point";
    }

    @GetMapping("my/coupon")
    public String coupon(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/coupon";
    }

    @GetMapping("my/review")
    public String review(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/review";
    }

    @GetMapping("my/qna")
    public String qna(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/qna";
    }

    @GetMapping("my/info")
    public String info(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "my/info";
    }
}
