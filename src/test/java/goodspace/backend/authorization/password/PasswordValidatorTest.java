package goodspace.backend.authorization.password;

import goodspace.backend.global.password.PasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PasswordValidatorTest {
    @Autowired
    PasswordValidator passwordValidator;

    @Nested
    class isIllegalPassword {
        @Test
        @DisplayName("비밀번호가 너무 짧으면 true를 반환한다")
        void returnTrueIfShortPassword() {
            // given
            String shortPassword = "Hello1!";

            // when
            boolean illegalPassword = passwordValidator.isIllegalPassword(shortPassword);

            // then
            assertThat(illegalPassword).isTrue();
        }

        @Test
        @DisplayName("비밀번호에 알파벳이 없으면 true를 반환한다")
        void returnTrueIfHasNotAlphabetic() {
            // given
            String notAlphabeticPassword = "141516171819#";

            // when
            boolean illegalPassword = passwordValidator.isIllegalPassword(notAlphabeticPassword);

            // then
            assertThat(illegalPassword).isTrue();
        }

        @Test
        @DisplayName("비밀번호에 숫자가 없으면 true를 반환한다")
        void returnTrueIfHasNotDigit() {
            // given
            String notDigitPassword = "HelloWorldJava#";

            // when
            boolean illegalPassword = passwordValidator.isIllegalPassword(notDigitPassword);

            // then
            assertThat(illegalPassword).isTrue();
        }

        @Test
        @DisplayName("비밀번호에 특수문자가 없으면 true를 반환한다")
        void returnTrueIfHasNotSpecialCharacter() {
            // given
            String notSpecialCharacterPassword = "HelloWorldJava0490";

            // when
            boolean illegalPassword = passwordValidator.isIllegalPassword(notSpecialCharacterPassword);

            // then
            assertThat(illegalPassword).isTrue();
        }

        @Test
        @DisplayName("비밀번호에 연속 문자가 있으면 true를 반환한다")
        void returnTrueIfHasSequentialCharacters() {
            // given
            String sequentialPassword = "HelloWorld123#";

            // when
            boolean illegalPassword = passwordValidator.isIllegalPassword(sequentialPassword);

            // then
            assertThat(illegalPassword).isTrue();
        }
    }
}
