package academy.acceptance;

import static org.assertj.core.api.Assertions.*;

import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.parser.LogEntry;
import academy.parser.NginxLogParser;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LogFileParsingTest {

    //    @Test
    //    @DisplayName("На вход передан валидный локальный log-файл")
    //    void localFileProcessingTest() {

    //    }

    //    @Test
    //    @DisplayName("На вход передан валидный удаленный log-файл")
    //    void remoteFileProcessingTest() {
    //        fail("Not implemented yet");
    //    }
    //
    //    @Test
    //    @DisplayName("На вход передан валидный локальный log-файл, "
    //            + "часть строк в котором нужно отфильтровать по --from и --to")
    //    void localFileProcessingAndFilteringTest() {
    //        fail("Not implemented yet");
    //    }
    //
    //    @Test
    //    @DisplayName("На вход передан локальный log-файл, часть строк в котором не подходит под формат")
    //    void damagedLocalFileProcessingTest() {
    //        fail("Not implemented yet");
    //    }

    @Test
    @DisplayName("Парсит валидную строку из ТЗ")
    void shouldParseValidLineFromTask() {
        String line =
                "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3\"";

        var result = NginxLogParser.parse(line);

        assertThat(result).isPresent();
        LogEntry entry = result.get();

        assertThat(entry.remoteAddr()).isEqualTo("93.180.71.3");
        assertThat(entry.remoteUser()).isEqualTo("-");
        assertThat(entry.timestamp()).isEqualTo(Instant.parse("2015-05-17T08:05:32Z"));
        assertThat(entry.method()).isEqualTo("GET");
        assertThat(entry.resource()).isEqualTo("/downloads/product_1");
        assertThat(entry.protocol()).isEqualTo("HTTP/1.1");
        assertThat(entry.status()).isEqualTo(304);
        assertThat(entry.bodyBytesSent()).isEqualTo(0L);
        assertThat(entry.referer()).isEqualTo("-");
        assertThat(entry.userAgent()).isEqualTo("Debian APT-HTTP/1.3");
    }

    @ParameterizedTest
    @MethodSource("validLinesProvider")
    @DisplayName("Парсит различные валидные строки")
    void shouldParseValidLines(String line, LogEntry expected) {
        var result = NginxLogParser.parse(line);
        assertThat(result).contains(expected);
    }

    static Stream<Arguments> validLinesProvider() {
        return Stream.of(
                Arguments.of(
                        "127.0.0.1 - frank [10/Oct/2025:13:55:36 -0700] \"POST /api/user HTTP/1.0\" 200 1234 \"http://example.com\" \"Mozilla/5.0\"",
                        new LogEntry(
                                "127.0.0.1",
                                "frank",
                                Instant.parse("2025-10-10T13:55:36Z"),
                                "POST",
                                "/api/user",
                                "HTTP/1.0",
                                200,
                                1234L,
                                "http://example.com",
                                "Mozilla/5.0")),
                Arguments.of(
                        "192.168.1.1 - - [01/Jan/2025:00:00:00 +0000] \"GET /index.html HTTP/2.0\" 200 5678 \"-\" \"Chrome/120\"",
                        new LogEntry(
                                "192.168.1.1",
                                "-",
                                Instant.parse("2025-01-01T00:00:00Z"),
                                "GET",
                                "/index.html",
                                "HTTP/2.0",
                                200,
                                5678L,
                                "-",
                                "Chrome/120")));
    }

    @ParameterizedTest
    @MethodSource("invalidLinesProvider")
    @DisplayName("Возвращает empty и пишет WARN при невалидных строках")
    void shouldReturnEmptyOnInvalidLines(String line) {
        var result = NginxLogParser.parse(line);

        assertThat(result).isEmpty();
        // WARN уже в логах — в acceptance-тестах проверим
    }

    static Stream<String> invalidLinesProvider() {
        return Stream.of(
                "this is not a log line",
                "127.0.0.1 - - [01/Jan/2025:25:00:00 +0000] GET /test", // битое время
                "127.0.0.1 - - [01/Jan/2025:12:00:00 +0000] GET /test HTTP/1.1 200 123", // нет кавычек
                "", // пустая строка
                "   ", // пробелы
                "127.0.0.1 - - [01/Jan/2025:12:00:00 +0000] \"GET /test HTTP/1.1\" abc 123 \"-\" \"Agent\"", // статус
                // не число
                "127.0.0.1 - - [01/Jan/2025:12:00:00 +0000] \"GET /test HTTP/1.1\" 200 abc \"-\" \"Agent\"" // размер не
                // число
                );
    }

    @Test
    @DisplayName("Обрабатывает referer и userAgent с пробелами и кавычками")
    void shouldHandleComplexRefererAndAgent() {
        String line =
                "10.0.0.1 - user [02/Feb/2025:10:10:10 +0000] \"GET /page HTTP/1.1\" 200 999 \"https://ref.com/search?q=hello world\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64)\"";

        var result = NginxLogParser.parse(line);

        assertThat(result).isPresent();
        assertThat(result.get().referer()).isEqualTo("https://ref.com/search?q=hello world");
        assertThat(result.get().userAgent()).isEqualTo("Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
    }

    @Test
    @DisplayName("Кидает исключение при ошибке парсинга даты (не WARN, а ошибку)")
    void shouldThrowOnDateParseError() {
        String line = "127.0.0.1 - - [32/Jan/2025:12:00:00 +0000] \"GET /test HTTP/1.1\" 200 100 \"-\" \"Agent\"";

        assertThatThrownBy(() -> NginxLogParser.parse(line))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining(ErrorMessages.INVALID_FORMAT_NGINX)
                .hasFieldOrPropertyWithValue("exitCode", 2);
    }
}
