package academy.util;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import lombok.experimental.UtilityClass;

/** Утилита для парсинга дат из формата NGINX. */
@UtilityClass
public final class DateUtils {
    private static final DateTimeFormatter ISO8601_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.DAY_OF_MONTH) // принимает 1 или 01
            .appendLiteral('/')
            .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.SHORT)
            .appendLiteral('/')
            .appendValue(ChronoField.YEAR)
            .appendLiteral(':')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendLiteral(' ')
            .appendOffset("+HHMM", "Z")
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter ARGS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Парсинг даты из логов NGINX */
    public static Instant parseLogsDate(String date) {
        try {
            return LocalDateTime.parse(date, ISO8601_FORMATTER).toInstant(ZoneOffset.UTC);
        } catch (Exception exception) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_TIME_FORMAT + ": " + date, exception, ExitCode.INVALID_USAGE.getCode());
        }
    }

    /** Парсинг даты из аргументов CLI (--from, --to) */
    public static Instant parseArgsDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, ARGS_FORMATTER);
            return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            throw new LogAnalyzerException(ErrorMessages.INVALID_TIME_FORMAT + ": " + date, e, 2);
        }
    }
}
