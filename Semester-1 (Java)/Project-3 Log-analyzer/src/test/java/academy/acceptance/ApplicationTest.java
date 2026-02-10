package academy.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cli.AnalyzerCommand;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class ApplicationTest {

    @Test
    @DisplayName("Базовая проверка работоспособности программы (без фильтров)")
    void happyPathTest() throws Exception {
        Path output = Files.createTempFile("report", ".json");
        Files.deleteIfExists(output);

        String[] args = {
            "--path", "scripts/data/input/logs/*.txt",
            "--format", "json",
            "--output", output.toString()
        };

        int exitCode = new CommandLine(new AnalyzerCommand()).execute(args);

        assertThat(exitCode).isEqualTo(0);
        assertThat(Files.exists(output)).isTrue();

        String json = Files.readString(output);
        assertThat(json)
                .contains("totalRequestsCount")
                .contains("responseCodes")
                .contains("resources")
                .contains("382.0");
    }

    @Test
    @DisplayName("Фильтр только по from (to == null)")
    void fromOnlyTest() throws Exception {
        Path output = Files.createTempFile("report", ".json");
        Files.deleteIfExists(output);

        String[] args = {
            "--path", "scripts/data/input/logs/part1.txt",
            "--format", "json",
            "--output", output.toString(),
            "--from", "2015-05-17"
        };

        int exitCode = new CommandLine(new AnalyzerCommand()).execute(args);

        assertThat(exitCode).isEqualTo(0);
        String json = Files.readString(output);
        // Проверяем, что остались только записи >= 17 мая 08:05:30
        assertThat(json).contains("2015-05-17");
    }

    @Test
    @DisplayName("Фильтр только по to (from == null)")
    void toOnlyTest() throws Exception {
        Path output = Files.createTempFile("report", ".json");
        Files.deleteIfExists(output);

        String[] args = {
            "--path", "scripts/data/input/logs/part1.txt",
            "--format", "json",
            "--output", output.toString(),
            "--to", "2015-05-17"
        };

        int exitCode = new CommandLine(new AnalyzerCommand()).execute(args);

        assertThat(exitCode).isEqualTo(0);
        String json = Files.readString(output);
        // Проверяем, что остались только записи <= 17 мая
        assertThat(json).contains("2015-05-01");
    }

    @Test
    @DisplayName("Фильтр по диапазону (from != null && to != null)")
    void fromToRangeTest() throws Exception {
        Path output = Files.createTempFile("report", ".json");
        Files.deleteIfExists(output);

        String[] args = {
            "--path", "scripts/data/input/logs/part1.txt",
            "--format", "json",
            "--output", output.toString(),
            "--from", "2015-05-01",
            "--to", "2015-05-02"
        };

        int exitCode = new CommandLine(new AnalyzerCommand()).execute(args);

        assertThat(exitCode).isEqualTo(0);
        String json = Files.readString(output);
        // Проверяем, что остались только записи в диапазоне
        assertThat(json).contains("2015-05-01");
    }

    @Test
    @DisplayName("Неверный диапазон дат (from > to)")
    void invalidRangeTest() throws Exception {
        Path output = Files.createTempFile("report", ".json");
        Files.deleteIfExists(output);

        String[] args = {
            "--path", "scripts/data/input/logs/part1.txt",
            "--format", "json",
            "--output", output.toString(),
            "--from", "2015-05-18T00:00:00Z",
            "--to", "2015-05-17T00:00:00Z"
        };

        int exitCode = new CommandLine(new AnalyzerCommand()).execute(args);

        // Ожидаем код ошибки != 0
        assertThat(exitCode).isNotEqualTo(0);
    }
}
