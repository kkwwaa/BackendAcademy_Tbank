package academy.cli;

import academy.output.OutputFormat;
import lombok.Getter;
import lombok.ToString;
import picocli.CommandLine;

@Getter
@ToString
public final class Args {

    @CommandLine.Option(
            names = {"-c", "--class"},
            required = true,
            description = "Полное имя класса, например: java.util.ArrayList")
    private String className;

    @CommandLine.Option(
            names = {"-f", "--format"},
            required = false,
            description = "Формат отчёта: ${COMPLETION-CANDIDATES}",
            defaultValue = "TEXT")
    private OutputFormat format;
}
