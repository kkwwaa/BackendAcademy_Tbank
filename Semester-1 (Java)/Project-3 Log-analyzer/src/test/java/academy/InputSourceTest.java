package academy;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.io.input.FileSource;
import academy.io.input.InputSource;
import academy.io.input.LogSourceResolver;
import academy.io.input.UrlSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class InputSourceTest {

    @Test
    void fileSource_shouldReadExistingFile(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("access.log");
        Files.writeString(file, "line1\nline2\nline3");

        FileSource source = new FileSource(file);
        List<String> lines = source.lines().toList();

        assertThat(lines).containsExactly("line1", "line2", "line3");
    }

    @Test
    void fileSource_shouldThrowIfFileNotExists() {
        Path invalidPath = Path.of("nonexistent.log");
        LogAnalyzerException ex = assertThrows(LogAnalyzerException.class, () -> new FileSource(invalidPath));
        assertTrue(ex.getMessage().contains(ErrorMessages.INVALID_FILEPATH));
    }

    @Test
    void urlSource_shouldThrowOnInvalidUrl() {
        assertThatThrownBy(() -> new UrlSource("not-a-url"))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining(ErrorMessages.INVALID_URL);
    }

    @Test
    void logSourceResolver_shouldHandleSingleFile() {
        List<InputSource> sources = LogSourceResolver.resolve(List.of("scripts/data/input/logs/part1.txt"));

        assertThat(sources).hasSize(1).first().isInstanceOf(FileSource.class);
    }

    @Test
    void logSourceResolver_shouldSupportGlobPattern(@TempDir Path tempDir) throws Exception {
        Files.createFile(tempDir.resolve("access1.log"));
        Files.createFile(tempDir.resolve("access.txt"));

        List<InputSource> sources = LogSourceResolver.resolve(List.of(tempDir + "/*.log"));

        assertThat(sources).hasSize(1).allMatch(s -> s instanceof FileSource);
    }

    @Test
    void logSourceResolver_shouldSupportUrl() {
        List<InputSource> sources = LogSourceResolver.resolve(
                List.of(
                        "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs"));

        assertThat(sources).hasSize(1).first().isInstanceOf(UrlSource.class);
    }

    @Test
    void logSourceResolver_shouldThrowIfNoSourcesFound() {
        assertThatThrownBy(() -> LogSourceResolver.resolve(List.of("nonexistent/*.log")))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining(ErrorMessages.INVALID_FILEPATH);
    }

    @Test
    void logSourceResolver_shouldThrowIfPathIsInvalid() {
        assertThatThrownBy(() -> LogSourceResolver.resolve(List.of("/invalid/path/log.log")))
                .isInstanceOf(LogAnalyzerException.class)
                .hasMessageContaining(ErrorMessages.INVALID_FILEPATH);
    }

    @Test
    void logSourceResolver_shouldHandleMixedSources() {
        List<InputSource> sources = LogSourceResolver.resolve(List.of(
                "scripts/data/input/logs/part1.txt", "https://example.com/log.log", "scripts/data/input/logs/*.txt"));

        assertThat(sources)
                .hasSizeGreaterThanOrEqualTo(2)
                .anyMatch(s -> s instanceof FileSource)
                .anyMatch(s -> s instanceof UrlSource);
    }
}
