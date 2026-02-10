package academy.cli;

import academy.cli.args.Args;
import academy.cli.args.ArgsValidator;
import academy.config.LogConfig;
import academy.io.input.InputSource;
import academy.io.input.LogSourceResolver;
import academy.parser.NginxLogParser;
import academy.report.ReportData;
import academy.report.ReportWriter;
import academy.report.ReportWriterFactory;
import academy.stats.LogStatistics;
import academy.util.DateUtils;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;

public final class AnalyzerService {
    private static final Logger LOG = LogConfig.LOG;

    public ReportData analyze(Args args) throws IOException {
        LOG.info("Начало анализа. Аргументы: {}", args);

        // Валидация аргументов
        ArgsValidator.validate(args);
        LOG.debug("Аргументы прошли валидацию");

        // Разрешение источников
        List<InputSource> sources = LogSourceResolver.resolve(args.getPaths());
        LOG.info("Найдено {} источников для анализа: {}", sources.size(), args.getPaths());

        // Подготовка статистики и фильтра
        LogStatistics statistics = new LogStatistics();

        Instant fromDate = args.getFrom() != null ? DateUtils.parseArgsDate(args.getFrom()) : null;
        Instant toDate = args.getTo() != null ? DateUtils.parseArgsDate(args.getTo()) : null;
        LogFilter filter = LogFilter.of(fromDate, toDate);
        LOG.info("Фильтр дат: fromDate={}, toDate={}", fromDate, toDate);

        // Потоковая обработка
        for (InputSource source : sources) {
            statistics.addPath(source);
            LOG.info("Обработка источника: {}", source);
            long processed;
            try (Stream<String> lines = source.lines()) {
                processed = lines.map(NginxLogParser::parse)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(filter)
                        .peek(entry -> LOG.trace("Принят лог: {}", entry))
                        .peek(statistics::accept)
                        .count();
            }
            LOG.info("Источник {} обработан, принято {} записей", source, processed);
        }

        ReportData report = statistics.build();
        LOG.info("Анализ завершён. Всего запросов: {}", report.totalRequestsCount());
        return report;
    }

    public void writeReport(ReportData report, Args args) {
        ReportWriter writer = ReportWriterFactory.forFormat(args.getFormat());
        LOG.info("Сохранение отчёта в формате {} по пути {}", args.getFormat(), args.getOutput());
        writer.write(report, args.getOutput());
        LOG.info("Отчёт успешно сохранён: {}", args.getOutput());
    }
}
