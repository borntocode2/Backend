package goodspace.backend.email.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MessageBuilderImpl implements MessageBuilder {
    @Override
    public void fillContent(MimeMessage message, String to, String code, int expireMinute) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject("[Good Space] 이메일 인증 코드");
        helper.setText(
                String.format("안녕하세요!%n%n인증 코드는 다음과 같습니다:%n%n  %s  %n%n(%d분 후 만료됩니다)", code, expireMinute),
                false
        );
    }
}
