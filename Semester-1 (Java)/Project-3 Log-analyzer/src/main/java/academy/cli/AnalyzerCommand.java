package academy.cli;

import academy.cli.args.Args;
import academy.config.ExitCode;
import academy.config.LogConfig;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.report.ReportData;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

/** Главная CLI-команда для запуска анализатора логов. Использует picocli для парсинга аргументов. */
@CommandLine.Command(
        name = "log-analyzer",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Анализатор логов NGINX")
public final class AnalyzerCommand implements Callable<Integer> {

    @CommandLine.Mixin
    private Args args;

    private static final Logger LOG = LogConfig.LOG;
    private final AnalyzerService service = new AnalyzerService();

    @Override
    public Integer call() {
        try {
            LogConfig.init();
            LOG.info("Запуск анализатора логов");

            ReportData report = service.analyze(args);
            service.writeReport(report, args);

            LOG.info("Анализ завершён успешно");
            return ExitCode.SUCCESS.getCode();
        } catch (LogAnalyzerException exception) {
            LOG.error("Ошибка: {}", exception.getMessage());
            return exception.getExitCode();
        } catch (Exception exception) {
            LOG.error(ErrorMessages.UNEXPECTED_READ_URL_ERROR, exception);
            return ExitCode.UNEXPECTED_ERROR.getCode();
        }
    }
}
