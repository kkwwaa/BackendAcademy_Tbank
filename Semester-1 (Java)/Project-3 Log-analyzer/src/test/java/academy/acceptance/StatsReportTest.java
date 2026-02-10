package academy.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import academy.io.output.AsciiDocReportWriter;
import academy.io.output.JsonReportWriter;
import academy.io.output.markdownReportWriter.MarkdownReportWriter;
import academy.report.ReportData;
import academy.report.utils.RequestPerDateStat;
import academy.report.utils.ResourceStat;
import academy.report.utils.ResponseCodeStat;
import academy.report.utils.ResponseSize;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StatsReportTest {

    private ReportData sampleReport() {
        List<String> files = List.of("access.log", "http://example.com/access.log");
        long totalRequestsCount = 3;

        ResponseSize responseSize = new ResponseSize(2000, 5000, 5000);

        List<ResourceStat> resources = List.of(new ResourceStat("/api", 2), new ResourceStat("/upload", 1));

        List<ResponseCodeStat> responseCodes = List.of(new ResponseCodeStat(200, 2), new ResponseCodeStat(201, 1));

        List<RequestPerDateStat> requestsPerDate = List.of(
                new RequestPerDateStat("2025-01-01", "Wednesday", 2, 66.67),
                new RequestPerDateStat("2025-01-02", "Thursday", 1, 33.33));

        List<String> uniqueProtocols = List.of("HTTP/1.1", "HTTP/2.0");

        return new ReportData(
                files, totalRequestsCount, responseSize, resources, responseCodes, requestsPerDate, uniqueProtocols);
    }

    @Test
    @DisplayName("Сохранение статистики в формате JSON")
    void jsonTest() throws Exception {
        ReportData report = sampleReport();
        Path out = Files.createTempFile("report", ".json");
        Files.deleteIfExists(out);

        new JsonReportWriter().write(report, out);

        String content = Files.readString(out);
        assertThat(content)
                .contains("\"files\"")
                .contains("\"totalRequestsCount\"")
                .contains("\"responseSizeInBytes\"")
                .contains("\"resources\"")
                .contains("\"responseCodes\"")
                .contains("\"requestsPerDate\"")
                .contains("\"uniqueProtocols\"");
    }

    @Test
    @DisplayName("Сохранение статистики в формате MARKDOWN")
    void markdownTest() throws Exception {
        ReportData report = sampleReport();
        Path out = Files.createTempFile("report", ".md");
        Files.deleteIfExists(out);

        new MarkdownReportWriter().write(report, out);

        String content = Files.readString(out);
        assertThat(content)
                .contains("#### Общая информация")
                .contains("Количество запросов")
                .contains("Средний размер ответа")
                .contains("Максимальный размер ответа")
                .contains("|")
                .contains("5000.0b")
                .contains("200")
                .contains("201")
                .contains("#### Запрашиваемые ресурсы")
                .contains("/api")
                .contains("/upload");
    }

    @Test
    @DisplayName("Сохранение статистики в формате ADOC")
    void adocTest() throws Exception {
        ReportData report = sampleReport();
        Path out = Files.createTempFile("report", ".adoc");
        Files.deleteIfExists(out);

        new AsciiDocReportWriter().write(report, out);

        String content = Files.readString(out);
        assertThat(content)
                .contains("== Общая информация")
                .contains("Количество запросов: 3")
                .contains("Средний размер ответа: 2000.0b")
                .contains("Максимальный размер ответа: 5000.0b")
                .contains("95p размера ответа: 5000.0b")
                .contains("== Коды ответа")
                .contains("200 → 2")
                .contains("201 → 1")
                .contains("== Запрашиваемые ресурсы")
                .contains("/api → 2")
                .contains("/upload → 1")
                .contains("== Протоколы")
                .contains("HTTP/1.1")
                .contains("HTTP/2.0")
                .contains("== Запросы по датам")
                .contains("2025-01-01 (Wednesday)")
                .contains("2025-01-02 (Thursday)");
    }
}
