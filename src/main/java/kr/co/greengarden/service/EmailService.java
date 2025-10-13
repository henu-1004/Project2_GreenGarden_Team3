package kr.co.greengarden.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import kr.co.greengarden.dto.SessionDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;



    private final SessionDataDTO sessionData;


    public void sendCode(String receiver){

        MimeMessage message = mailSender.createMimeMessage();

        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        String title = "[Green Garden] 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.</h1>";

        try {
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            // 메일 전송
            mailSender.send(message);

            // 현재 세션 저장(현재 클라이언트)
            //session.setAttribute("sessCode", String.valueOf(code));
            sessionData.setCode(String.valueOf(code));

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


    public boolean verifyCode(String code){

        // 현재 세션 코드 가져오기
        //String sessCode = (String) session.getAttribute("sessCode");
        String sessCode = sessionData.getCode();

        if(sessCode.equals(code)){
            return true;
        }

        return false;
    }
}
