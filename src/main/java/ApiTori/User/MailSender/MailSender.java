package ApiTori.User.MailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Component("myMailSender")
public class MailSender {

    public final Map<String, String> dictionaryForEmails = new HashMap<>();
    private final JavaMailSender javaMailSender;

    @Autowired
    public MailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String emailUsername;

    public void sendVerificationCode(String toEmail) {
        String verificationCode = generateVerificationCode();
        dictionaryForEmails.put(toEmail, verificationCode);

        sendEmail(toEmail, "Код подтверждения", "Ваш код подтверждения: " + verificationCode);
        System.out.println(verificationCode + " code");
    }

    private String generateVerificationCode() {
        int code = ThreadLocalRandom.current().nextInt(1000, 10000);
        return String.valueOf(code);
    }

    private void sendEmail(String toEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(emailUsername);

        javaMailSender.send(mailMessage);
    }
}