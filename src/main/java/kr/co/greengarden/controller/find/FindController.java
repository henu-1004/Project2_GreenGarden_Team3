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

import java.util.List;
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


    // 아이디 찾기 - 인증번호 전송
    @ResponseBody
    @PostMapping("/find/userId/sendCode")
    public ResponseEntity<?> sendCode(@RequestParam String email, HttpSession session){

        String code = emailService.sendCodeAndReturn(email);
        session.setAttribute("findEmail", email);
        session.setAttribute("findCode", code);

        log.info("[FIND:SEND] email={}, savedCode={}", email, code);
        return ResponseEntity.ok(Map.of("result", "OK"));
    }

    // 인증번호 확인
    @ResponseBody
    @PostMapping("/find/userId/verifyCode")
    public ResponseEntity<Boolean> verifyCode(@RequestParam String email,
                                              @RequestParam String code,
                                              HttpSession session){

        String savedEmail = (String) session.getAttribute("findEmail");
        String savedCode = (String) session.getAttribute("findCode");

        boolean emailOk = savedEmail != null && savedEmail.trim().equalsIgnoreCase(email.trim());
        boolean codeOk  = savedCode  != null && savedCode.equals(code);

        boolean verified = emailOk && codeOk;
        log.info("[FIND:VERIFY] reqEmail={}, reqCode={}, savedEmail={}, savedCode={}, result={}",
                email, code, savedEmail, savedCode, verified);

        if (verified) session.setAttribute("verified", true);

        return ResponseEntity.ok(verified);

    }

    // 아이디 결과
    @PostMapping("/find/userId/result")
    public String result(@RequestParam String name,
                         @RequestParam String email,
                         Model model,
                         HttpSession session){

        log.info("[find/userId/result] raw name='{}', email='{}'", name, email);

        Optional<FindResultDTO> result = memberGeneralService.findMemberInfoByNameAndEmail(name, email);

        // 결과 로깅
        log.info("query present? {}", result.isPresent());
        result.ifPresent(r ->
                log.info("FOUND -> name='{}', email='{}', memId='{}', joinDate={}",
                        r.getName(), r.getEmail(), r.getMemId(), r.getJoinDate())
        );

        if(result.isPresent()){
            model.addAttribute("info", result.get());
        }else{
            model.addAttribute("error", "일치하는 회원 정보가 없습니다.");
        }

        return "find/resultId";
    }

    // 아이디 찾기 - 휴대폰
    @PostMapping("/find/userId/result-phone")
    public String resultByPhone(@RequestParam String name,
                                @RequestParam String phone,
                                Model model){
        var opt = memberGeneralService.findMemberInfoByNameAndPhone(name, phone);
        if(opt.isPresent()){
            model.addAttribute("info", opt.get());

        }else{
            model.addAttribute("error", "일치하는 회원 정보가 없습니다.");
        }
        return "find/resultId";
    }

    @ResponseBody
    @PostMapping("/find/userId/verifyPhone")
    public ResponseEntity<Boolean> verifyPhone(@RequestParam String name,
                                               @RequestParam String phone){
        boolean ok = memberGeneralService.verifyPhone(name, phone);
        return ResponseEntity.ok(ok);
    }

    // 비밀번호 찾기 - 인증번호 전송
    @ResponseBody
    @PostMapping("/find/password/sendCode")
    public ResponseEntity<?> pwSendCode(@RequestParam String email, HttpSession session){

        String code = emailService.sendCodeAndReturn(email);
        session.setAttribute("pwFindEmail", email);
        session.setAttribute("pwFindCode", code);

        log.info("[PW:SEND] to={}, savedCode={}", email, code);
        return ResponseEntity.ok(Map.of("result", "OK"));
    }

    // 인증번호 확인
    @ResponseBody
    @PostMapping("/find/password/verifyCode")
    public ResponseEntity<Boolean> pwVerify(@RequestParam String email,
                                            @RequestParam String code,
                                            HttpSession session){

        String savedEmail = (String) session.getAttribute("pwFindEmail");
        String savedCode = (String) session.getAttribute("pwFindCode");

        boolean emailOk = savedEmail != null && savedEmail.trim().equalsIgnoreCase(email.trim());
        boolean codeOk  = savedCode  != null && savedCode.equals(code);

        boolean verified = emailOk && codeOk;

        log.info("[PW:VERIFY] reqEmail={}, reqCode={}, savedEmail={}, savedCode={}, result={}",
                email, code, savedEmail, savedCode, verified);

        if(verified) session.setAttribute("pwVerified", true);
        return ResponseEntity.ok(verified);
    }

    // 비밀번호
    @PostMapping("/find/password/confirm")
    public String pwConfirm(@RequestParam String memId,
                            @RequestParam String email,
                            HttpSession session,
                            Model model){

        if(!Boolean.TRUE.equals(session.getAttribute("pwVerified"))) {
            model.addAttribute("error", "이메일 인증을 먼저 완료하세요.");
            model.addAttribute("memId", memId);
            model.addAttribute("email", email);
            return "find/password";
        }

        if(!memberGeneralService.canResetPassword(memId, email)) {
            model.addAttribute("error", "아이디와 이메일이 일치하지 않습니다.");
            model.addAttribute("memId", memId);
            model.addAttribute("email", email);
            return "find/password";
        }

        session.setAttribute("pwTargetId", memId);
        return "find/changePassword";

    }

    // 비밀번호 변경
    @PostMapping("/find/changePassword")
    public String doChangePassword(@RequestParam String newPassword,
                                   @RequestParam String confirmPassword,
                                   HttpSession session,
                                   Model model){

        String memId = (String) session.getAttribute("pwTargetId");
        Boolean verified = (Boolean) session.getAttribute("pwVerified");

        if(memId == null || verified == null || !verified) {
            model.addAttribute("error", "비밀번호를 재설정 하세요.");
            return "find/password";
        }

        if(newPassword == null || !newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 다릅니다.");
            return "find/password";
        }

        memberGeneralService.changePassword(memId, newPassword);

        session.removeAttribute("pwTargetId");
        session.removeAttribute("pwVerified");
        session.removeAttribute("pwFindEmail");
        session.removeAttribute("pwFindCode");

        return "redirect:/member/login";

    }










}
