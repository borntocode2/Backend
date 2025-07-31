package goodspace.backend.global.password;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PasswordValidatorImpl implements PasswordValidator {
    private final int MINIMUM_LENGTH = 10;
    private static final Set<Character> specialChars = Set.of(
            '!', '#', '$', '%', '^', '&', '*',
            '(', ')', '-', '_', '=', '+', '[',
            ']', '{', '}', ';', ':', '\'', '"',
            ',', '.', '<', '>', '/', '?', '\\',
            '`', '~'
    );

    @Override
    public boolean isIllegalPassword(String password) {
        return isShortThanMiniMum(password) ||
                isNotContainAlphabetic(password) ||
                isNotContainDigit(password) ||
                isNotContainSpecialCharacter(password) ||
                isEmailFormat(password) ||
                hasSequentialCharacters(password);
    }

    private boolean isShortThanMiniMum(String password) {
        return password.length() < MINIMUM_LENGTH;
    }

    private boolean isNotContainAlphabetic(String password) {
        for (char word : password.toCharArray()) {
            if (Character.isAlphabetic(word)) {
                return false;
            }
        }

        return true;
    }

    private boolean isNotContainDigit(String password) {
        for (char word : password.toCharArray()) {
            if (Character.isDigit(word)) {
                return false;
            }
        }

        return true;
    }

    private boolean isNotContainSpecialCharacter(String password) {
        for (char word : password.toCharArray()) {
            if (specialChars.contains(word)) {
                return false;
            }
        }

        return true;
    }

    private boolean isEmailFormat(String password) {
        return password.contains("@");
    }

    private boolean hasSequentialCharacters(String password) {
        String lower = password.toLowerCase();
        for (int i = 0; i < lower.length() - 2; i++) {
            char c1 = lower.charAt(i);
            char c2 = lower.charAt(i + 1);
            char c3 = lower.charAt(i + 2);

            if (c2 == c1 + 1 && c3 == c2 + 1) {
                return true;
            }
            if (c2 == c1 - 1 && c3 == c2 - 1) {
                return true;
            }
        }

        return false;
    }
}
