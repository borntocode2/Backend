package goodspace.backend.email.domain;

import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.fixture.EmailVerificationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EmailVerificationCleanerTest {
    @Autowired
    EmailVerificationCleaner emailVerificationCleaner;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;

    EmailVerification expiredEmailVerification;
    EmailVerification notExpriedEmailVerification;
    EmailVerification expiredAndVerifedEmailVerification;

    @BeforeEach
    void resetEntities() {
        expiredEmailVerification = emailVerificationRepository.save(EmailVerificationFixture.EXPIRED.getInstance());
        notExpriedEmailVerification = emailVerificationRepository.save(EmailVerificationFixture.NOT_EXPIRED.getInstance());
        expiredAndVerifedEmailVerification = emailVerificationRepository.save(EmailVerificationFixture.EXPIRED_AND_VERIFIED.getInstance());
    }

    @Nested
    class clean {
        @Test
        @DisplayName("만료된 이메일 인증 정보를 제거한다")
        void removeExpiredEmailVerification() {
            emailVerificationCleaner.clean();

            boolean expiredIsDeleted = emailVerificationRepository.findById(expiredEmailVerification.getId())
                    .isEmpty();
            assertThat(expiredIsDeleted).isTrue();
        }

        @Test
        @DisplayName("만료됐더라도 인증된 이메일이라면 제거하지 않는다")
        void notRemoveIfVerifiedEmailVerification() {
            emailVerificationCleaner.clean();

            boolean verifiedIsDeleted = emailVerificationRepository.findById(expiredAndVerifedEmailVerification.getId())
                    .isEmpty();
            assertThat(verifiedIsDeleted).isFalse();
        }

        @Test
        @DisplayName("만료되지 않은 이메일 인증 정보는 제거하지 않는다")
        void notRemoveIfNotExpiredEmailVerification() {
            emailVerificationCleaner.clean();

            boolean notExpiredIsDeleted = emailVerificationRepository.findById(notExpriedEmailVerification.getId())
                    .isEmpty();
            assertThat(notExpiredIsDeleted).isFalse();
        }
    }
}
