package goodspace.backend.email.domain;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomCodeGenerator implements CodeGenerator {
    @Override
    public String generate(int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }
}
