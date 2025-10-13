package kr.co.greengarden.controller.find;

import jakarta.servlet.http.HttpSession;
import kr.co.greengarden.dto.FindResultDTO;
import kr.co.greengarden.service.EmailService;
import kr.co.greengarden.service.MemberGeneralService;
import kr.co.greengarden.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FindController {

    private final EmailService emailService;
    private final MemberGeneralService memberGeneralService;

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


    // 인증번호 전송
    @ResponseBody
    @PostMapping("/find/userId/sendCode")
    public ResponseEntity<?> sendCode(@RequestParam String email, HttpSession session){

        String code = String.format("%06d", new Random().nextInt(999999));
        session.setAttribute("authCode", code);
        session.setAttribute("authEmail", email);

        // 실제 이메일 전송
        emailService.sendCode(email);
        log.info("아이디 찾기용 인증번호 전송: {} ({})", email, code);

        return ResponseEntity.ok(Map.of("result", "OK"));
    }

    // 인증번호 확인

    @ResponseBody
    @PostMapping("/find/userId/verifyCode")
    public ResponseEntity<Boolean> verifyCode(@RequestParam String email,
                                              @RequestParam String code,
                                              HttpSession session){

        String savedEmail = (String) session.getAttribute("authEmail");
        String savedCode = (String) session.getAttribute("authCode");

        boolean verified = email.equals(savedEmail) && code.equals(savedCode);
        if(verified) session.setAttribute("verified", true);

        return ResponseEntity.ok(verified);

    }

    // 아이디 결과
    @PostMapping("/find/userId/result")
    public String result(@RequestParam String name,
                         @RequestParam String email,
                         Model model){
        Optional<FindResultDTO> result = memberGeneralService.findMemberInfoByNameAndEmail(name, email);

        if(result.isPresent()){
            model.addAttribute("info", result.get());
        }else{
            model.addAttribute("error", "일치하는 회원 정보가 없습니다.");
        }

        return "find/resultId";
    }

}
