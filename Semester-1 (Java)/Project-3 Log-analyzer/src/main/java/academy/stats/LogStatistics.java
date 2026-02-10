package academy.stats;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.io.input.FileSource;
import academy.io.input.InputSource;
import academy.io.input.UrlSource;
import academy.parser.LogEntry;
import academy.report.ReportData;
import academy.report.utils.RequestPerDateStat;
import academy.report.utils.ResourceStat;
import academy.report.utils.ResponseCodeStat;
import academy.report.utils.ResponseSize;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/** Главный агрегатор статистики. Принимает LogEntry и обновляет все счётчики. */
public class LogStatistics {
    private final List<String> paths = new ArrayList<>();
    private long totalRequests = 0;
    private final List<Long> responseSizes = new ArrayList<>();
    private final Map<String, Long> resourceCounter = new HashMap<>();
    private final Map<Integer, Long> codeCounter = new HashMap<>();
    private final Map<LocalDate, Long> dateCounter = new HashMap<>();
    private final Set<String> protocols = new HashSet<>();

    public void addPath(InputSource source) {
        if (source instanceof FileSource(Path path)) {
            if (path != null) {
                Path fileName = path.getFileName();
                paths.add(Objects.requireNonNullElse(fileName, path).toString());
            } else {
                throw new LogAnalyzerException(ErrorMessages.INPUT_SOURCE_PATH_NULL, ExitCode.INVALID_USAGE.getCode());
            }
        } else if (source instanceof UrlSource urlSource) {
            URL url = urlSource.getUrl();
            paths.add(url.toString());
        } else {
            paths.add(source.toString());
        }
    }

    public void accept(LogEntry entry) {
        totalRequests++;
        responseSizes.add(entry.bodyBytesSent());

        resourceCounter.merge(entry.resource(), 1L, Long::sum);

        codeCounter.merge(entry.status(), 1L, Long::sum);

        LocalDate date = entry.timestamp().atZone(java.time.ZoneOffset.UTC).toLocalDate();
        dateCounter.merge(date, 1L, Long::sum);

        protocols.add(entry.protocol());
    }

    private double averageResponseSize() {
        if (responseSizes.isEmpty()) return 0;
        double avg = responseSizes.stream().mapToLong(Long::longValue).average().orElse(0);
        return Math.round(avg * 100.0) / 100.0; // округляем до 2 знаков
    }

    private double maxResponseSize() {
        if (responseSizes.isEmpty()) return 0;
        double max = responseSizes.stream().mapToLong(Long::longValue).max().orElse(0);
        return Math.round(max * 100.0) / 100.0;
    }

    private double p95ResponseSize() {
        if (responseSizes.isEmpty()) {
            return 0;
        }

        // Отсортированный список размеров ответов (в байтах)
        List<Long> sortedResponseSizes = responseSizes.stream().sorted().toList();

        int totalCount = sortedResponseSizes.size();

        // Формула Hyndman–Fan type 7 (Excel inclusive / NumPy default):
        // h = (n - 1) * p + 1, где p = 0.95
        double fractionalRank = (totalCount - 1) * 0.95 + 1.0;

        // Целая часть ранга
        int lowerIndex = (int) Math.floor(fractionalRank);

        // Дробная часть ранга (вес интерполяции)
        double interpolationWeight = fractionalRank - lowerIndex;

        // Значение на позиции lowerIndex (с поправкой на 0-based индексацию)
        double lowerValue = sortedResponseSizes.get(lowerIndex - 1);

        // Значение на позиции upperIndex (следующий элемент)
        double upperValue = sortedResponseSizes.get(Math.min(lowerIndex, totalCount - 1));

        // Интерполяция между lowerValue и upperValue
        double percentileValue = lowerValue + interpolationWeight * (upperValue - lowerValue);

        // Округление до двух знаков после запятой
        return Math.round(percentileValue * 100.0) / 100.0;
    }

    public ReportData build() {
        ResponseSize responseSize = new ResponseSize(averageResponseSize(), maxResponseSize(), p95ResponseSize());

        List<ResourceStat> resourceStats = resourceCounter.entrySet().stream()
                .map(e -> new ResourceStat(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        List<ResponseCodeStat> codeStats = codeCounter.entrySet().stream()
                .map(e -> new ResponseCodeStat(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        List<RequestPerDateStat> dateStats = dateCounter.entrySet().stream()
                .map(e -> {
                    double percentage = (double) e.getValue() * 100.0 / totalRequests;
                    return new RequestPerDateStat(
                            e.getKey().toString(),
                            e.getKey().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                            e.getValue(),
                            Math.round(percentage * 100.0) / 100.0 // округляем до 2 знаков
                            );
                })
                .collect(Collectors.toList());

        return new ReportData(
                paths, totalRequests, responseSize, resourceStats, codeStats, dateStats, new ArrayList<>(protocols));
    }
}
