package goodspace.backend.email.domain;

import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailVerificationCleanerImpl implements EmailVerificationCleaner {
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    @Transactional
    public void clean() {
        List<EmailVerification> deletableEmailVerifications = emailVerificationRepository.findAll().stream()
                .filter(this::isHasToBeDeleted)
                .toList();

        emailVerificationRepository.deleteAll(deletableEmailVerifications);
    }

    private boolean isHasToBeDeleted(EmailVerification emailVerification) {
        return !emailVerification.isVerified() && emailVerification.isExpired(LocalDateTime.now());
    }
}
