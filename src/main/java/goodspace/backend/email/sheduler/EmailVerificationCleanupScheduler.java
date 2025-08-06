package goodspace.backend.email.sheduler;

import goodspace.backend.email.domain.EmailVerificationCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EmailVerificationCleanupScheduler {
    private final EmailVerificationCleaner emailVerificationCleaner;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void regularlyCleanEmailVerifications() {
        emailVerificationCleaner.clean();
    }
}
