package kr.co.greengarden.controller.policy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class PolicyController {

    @GetMapping("/policy/buyer")
    public String buyer(){
        return "policy/buyer";
    }

    @GetMapping("/policy/seller")
    public String seller(){
        return "policy/seller";
    }

    @GetMapping("/policy/finance")
    public String finance(){
        return "policy/finance";
    }

    @GetMapping("/policy/location")
    public String location(){
        return "policy/location";
    }

    @GetMapping("/policy/privacy")
    public String privacy(){
        return "policy/privacy";
    }
}
