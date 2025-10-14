package kr.co.greengarden.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.greengarden.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email/code")
    public ResponseEntity<Map<String, Boolean>> verify(@RequestBody Map<String, String> jsonData,
                                                       HttpSession session) {

        String code = jsonData.get("code");
        log.info("code:{}", code);

        boolean result = emailService.verifyCode(code);

        Map<String, Boolean> resultMap = Map.of("isMatched", result);

        return ResponseEntity.ok(resultMap);

    }

}
