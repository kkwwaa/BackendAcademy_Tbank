package academy.cli.args;

import java.nio.file.Path;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import picocli.CommandLine.Option;

@Getter
@ToString
public final class Args {
    @Option(
            names = {"-p", "--path"},
            required = true,
            split = ",",
            paramLabel = "PATH",
            description = "Путь к лог-файлам (локальные или URL). Можно несколько через запятую.")
    private List<String> paths;

    @Option(
            names = {"-f", "--format"},
            required = true,
            description = "Формат отчёта: ${COMPLETION-CANDIDATES}")
    private OutputFormat format;

    @Option(
            names = {"-o", "--output"},
            required = true,
            paramLabel = "OUTPUT",
            description = "Путь к файлу отчёта (расширение должно соответствовать формату)")
    private Path output;

    @Option(names = "--from", description = "Начальная дата в формате ISO8601 (включительно)")
    private String from;

    @Option(names = "--to", description = "Конечная дата в формате ISO8601 (включительно)")
    private String to;
}
