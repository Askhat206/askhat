package kz.aptekaplus.util;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GmailSMSSender {
    private final JavaMailSender mailSender;
    @Value("{$spring.mail.username}")
    private String fromMail;
    private final Logger log = LogManager.getLogger(GmailSMSSender.class);
    public void smsSender(String toGmail, String verificationCode){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("verification email");
        simpleMailMessage.setText(String.format(
                "code: %s", verificationCode
        ));
        simpleMailMessage.setTo(toGmail);
        log.info("send code to gmail: {}", toGmail);
        mailSender.send(simpleMailMessage);
    }
}
