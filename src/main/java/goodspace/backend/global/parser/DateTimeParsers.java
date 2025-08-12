package goodspace.backend.global.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeParsers {
    // yyyy-MM-dd'T'HH:mm:ss[.fraction] + offset(: 포함)
    private static final DateTimeFormatter OFFSET_WITH_COLON = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .appendOffset("+HH:MM", "Z")
            .toFormatter();

    // yyyy-MM-dd'T'HH:mm:ss[.fraction] + offset(: 없이)
    private static final DateTimeFormatter OFFSET_WITHOUT_COLON = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .appendOffset("+HHMM", "Z")
            .toFormatter();

    // 드물지만 +09 같은 형태 지원
    private static final DateTimeFormatter OFFSET_HOURS_ONLY = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .appendOffset("+HH", "Z")
            .toFormatter();

    private static final List<DateTimeFormatter> CANDIDATES = List.of(
            OFFSET_WITH_COLON,
            OFFSET_WITHOUT_COLON,
            OFFSET_HOURS_ONLY
    );

    public static OffsetDateTime parseOffsetDateTime(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        for (DateTimeFormatter parseFormat : CANDIDATES) {
            try {
                return OffsetDateTime.parse(text, parseFormat);
            } catch (DateTimeParseException ignore) { /* try next */ }
        }

        return OffsetDateTime.parse(text);
    }
}
