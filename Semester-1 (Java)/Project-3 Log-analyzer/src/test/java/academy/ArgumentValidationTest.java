package academy;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.IterableAssert.assertThatIterable;

import academy.cli.args.Args;
import academy.cli.args.ArgsValidator;
import academy.cli.args.OutputFormat;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.io.input.LogSourceResolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import picocli.CommandLine;

public class ArgumentValidationTest {
    private final Args args = new Args();
    private final CommandLine cmd = new CommandLine(args);

    @Test
    void shouldParseMultiplePathsSeparatedByComma() {
        cmd.execute(
                "--path", "access.log,logs/*.log,https://example.com/nginx.log",
                "--output", "report.json");

        assertThatIterable(args.getPaths())
                .hasSize(3)
                .containsExactly("access.log", "logs/*.log", "https://example.com/nginx.log");
    }

    @Test
    void WithoutFormatException() {
        cmd.execute("--path", "test.log", "--output", "out.json");
        assertThatThrownBy(() -> ArgsValidator.validate(args)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldAcceptMarkdownFormat() {
        cmd.execute("--path", "a.log", "--format", "markdown", "--output", "report.md");

        assertThat(args.getFormat()).isEqualTo(OutputFormat.MARKDOWN);
    }

    @Test
    void shouldParseFromAndToDates() {
        cmd.execute(
                "--path", "log.log",
                "--output", "r.json",
                "--from", "2025-01-01",
                "--to", "2025-12-31");

        assertThat(args.getFrom()).isEqualTo("2025-01-01");
        assertThat(args.getTo()).isEqualTo("2025-12-31");
    }

    @Test
    void shouldAllowOnlyFromDate() {
        cmd.execute("--path", "log.log", "--output", "r.json", "--from", "2025-03-01");

        assertThat(args.getFrom()).isEqualTo("2025-03-01");
        assertThat(args.getTo()).isNull();
    }

    @Test
    @DisplayName("На вход передан несуществующий локальный файл")
    void test1() {
        cmd.execute("--path", "nonexistent.log", "--output", "report.json");
        assertThat(args.getPaths()).contains("nonexistent.log");
    }

    @Test
    @DisplayName("На вход передан несуществующий удаленный файл")
    void test2() {
        cmd.execute("--path", "https://invalid.example.com/log.log", "--output", "report.json");
        assertThat(args.getPaths()).contains("https://invalid.example.com/log.log");
    }

    @Test
    @DisplayName("На вход передан существующий удаленный файл")
    void exitURL() {
        cmd.execute(
                "--path",
                "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
                "--output",
                "report.json",
                "-f",
                "json");

        assertThat(args.getPaths())
                .contains(
                        "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs");
        assertThatNoException().isThrownBy(() -> ArgsValidator.validate(args));
    }

    @ParameterizedTest
    @ValueSource(strings = {".docx", ""})
    @DisplayName("На вход передан файл в неподдерживаемом формате")
    void test3(String extension) {
        cmd.execute("--path", "file" + extension, "--output", "report.json", "-f", "json");

        assertThatThrownBy(() -> {
                    ArgsValidator.validate(args);
                    // теперь проверка расширения происходит при резолве источников
                    LogSourceResolver.resolve(args.getPaths());
                })
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining(ErrorMessages.UNSUPPORTED_FILE_EXTENSION);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2025.01.01", "2025-13-01", "yesterday", "01-01-2025"})
    @DisplayName("На вход переданы невалидные параметры --from / --to")
    void test4(String invalidDate) {
        cmd.execute("--path", "a.log", "--output", "r.json", "--from", invalidDate, "-f", "json");

        assertThatThrownBy(() -> ArgsValidator.validate(args))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining(ErrorMessages.INVALID_TIME_FORMAT);
    }

    @Test
    @DisplayName("--from и --to могут быть опущены (null)")
    void test4_nullAllowed() {
        cmd.execute("--path", "a.log", "--output", "r.json", "-f", "json");

        assertThatNoException().isThrownBy(() -> ArgsValidator.validate(args));
    }

    @ParameterizedTest
    @ValueSource(strings = {"xml", "TXT", "html", "csv", "yaml"})
    @DisplayName("Результаты запрошены в неподдерживаемом формате {0}")
    void test5(String format) {
        cmd.execute("--path", "a.log", "--output", "r.json", "--format", format);

        assertThatThrownBy(() -> ArgsValidator.validate(args)).isInstanceOf(NullPointerException.class);
    }

    private static Stream<Arguments> test6ArgumentsSource() {
        return Stream.of(
                Arguments.of("markdown", "results.txt"),
                Arguments.of("json", "results.md"),
                Arguments.of("adoc", "results.json"),
                Arguments.of("markdown", "report.JSON"),
                Arguments.of("adoc", "report.ad1"));
    }

    @ParameterizedTest
    @MethodSource("test6ArgumentsSource")
    @DisplayName("По пути в аргументе --output указан файл с некорректным расширением")
    void test6(String format, String output) {
        cmd.execute("--path", "a.log", "--format", format, "--output", output);

        assertThatThrownBy(() -> ArgsValidator.validate(args))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining("некорректным расширением");
    }

    @Test
    @DisplayName("По пути в аргументе --output уже существует файл")
    void test7(@TempDir Path tempDir) throws IOException {
        Path existingFile = tempDir.resolve("report.json");
        Files.createFile(existingFile);

        cmd.execute("--path", "a.log", "--output", existingFile.toString(), "-f", "json");

        assertThatThrownBy(() -> ArgsValidator.validate(args))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining("уже существует");
    }

    @ParameterizedTest
    @ValueSource(strings = {"--path", "--output", "-p", "-o"})
    @DisplayName("На вход не передан обязательный параметр")
    void test8(String missingArg) {
        String[] argsArray =
                switch (missingArg) {
                    case "--path", "-p" -> new String[] {"--output", "r.json"};
                    case "--output", "-o" -> new String[] {"--path", "a.log"};
                    default -> new String[0];
                };

        int exitCode = cmd.execute(argsArray);

        assertThat(exitCode).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"--input", "--filter", "--verbose", "-x"})
    @DisplayName("На вход передан неподдерживаемый параметр")
    void test9(String unknownArg) {
        int exitCode = cmd.execute("--path", "a.log", "--output", "r.json", unknownArg);

        assertThat(exitCode).isEqualTo(2);
    }

    @Test
    @DisplayName("Значение параметра --from больше, чем значение параметра --to")
    void test10() {
        cmd.execute(
                "--path", "a.log", "--output", "r.json", "--from", "2025-12-31", "--to", "2025-01-01", "-f", "json");

        assertThatThrownBy(() -> ArgsValidator.validate(args))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining(ErrorMessages.INVALID_DATE_RANGE);
    }
}
