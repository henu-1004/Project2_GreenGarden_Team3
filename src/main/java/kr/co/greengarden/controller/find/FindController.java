package kr.co.greengarden.controller.find;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class FindController {

    @GetMapping("/find/userId")
    public String userId(){
        return "find/userId";
    }

    @GetMapping("/find/resultId")
    public String resultId(){
        return "find/resultId";
    }

    @GetMapping("/find/password")
    public String password(){
        return "find/password";
    }

    @GetMapping("/find/changePassword")
    public String changePassword(){
        return "find/changePassword";
    }
}
