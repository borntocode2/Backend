package goodspace.backend.email.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MessageBuilder {
    void fillContent(MimeMessage mimeMessage, String to, String code, int expireMinute) throws MessagingException;
}
