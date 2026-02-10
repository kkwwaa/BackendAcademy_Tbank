package academy;

import static org.assertj.core.api.Assertions.assertThat;

import academy.io.output.AsciiDocReportWriter;
import academy.io.output.JsonReportWriter;
import academy.io.output.markdownReportWriter.MarkdownReportWriter;
import academy.report.ReportData;
import academy.report.ReportWriter;
import academy.report.utils.RequestPerDateStat;
import academy.report.utils.ResourceStat;
import academy.report.utils.ResponseCodeStat;
import academy.report.utils.ResponseSize;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportWriterTest {

    private ReportData sampleReport() {
        // Список источников (файлы/URL)
        List<String> files = List.of("access.log", "http://example.com/access.log");

        // Общее количество запросов
        long totalRequestsCount = 3;

        // Размеры ответа в байтах
        ResponseSize responseSize = new ResponseSize(
                2000, // average
                5000, // max
                5000 // p95
                );

        // Ресурсы
        List<ResourceStat> resources = List.of(new ResourceStat("/api", 2), new ResourceStat("/upload", 1));

        // Коды ответа
        List<ResponseCodeStat> responseCodes = List.of(new ResponseCodeStat(200, 2), new ResponseCodeStat(201, 1));

        // Запросы по датам
        List<RequestPerDateStat> requestsPerDate = List.of(
                new RequestPerDateStat("2025-01-01", "Wednesday", 2, 66.67),
                new RequestPerDateStat("2025-01-02", "Thursday", 1, 33.33));

        // Уникальные протоколы
        List<String> uniqueProtocols = List.of("HTTP/1.1", "HTTP/2.0");

        // Финальный отчёт
        return new ReportData(
                files, totalRequestsCount, responseSize, resources, responseCodes, requestsPerDate, uniqueProtocols);
    }

    @Test
    @DisplayName("JSON writer создаёт корректный файл")
    void jsonWriterCreatesFile() throws Exception {
        Path output = Files.createTempFile("report", ".json");
        Files.deleteIfExists(output); // чтобы writer мог создать заново

        ReportWriter writer = new JsonReportWriter();
        writer.write(sampleReport(), output);

        assertThat(Files.exists(output)).isTrue();
        String content = Files.readString(output);
        assertThat(content).contains("totalRequestsCount").contains("200").contains("/api");
    }

    @Test
    @DisplayName("Markdown writer создаёт корректный файл")
    void markdownWriterCreatesFile() throws Exception {
        Path output = Files.createTempFile("report", ".md");
        Files.deleteIfExists(output);

        ReportWriter writer = new MarkdownReportWriter();
        writer.write(sampleReport(), output);

        assertThat(Files.exists(output)).isTrue();
        String content = Files.readString(output);
        assertThat(content).contains("Количество запросов").contains("200 | 2").contains("/upload | 1");
    }

    @Test
    @DisplayName("AsciiDoc writer создаёт корректный файл")
    void asciidocWriterCreatesFile() throws Exception {
        Path output = Files.createTempFile("report", ".adoc");
        Files.deleteIfExists(output);

        ReportWriter writer = new AsciiDocReportWriter();
        writer.write(sampleReport(), output);

        assertThat(Files.exists(output)).isTrue();
        String content = Files.readString(output);
        assertThat(content).contains("== Общая информация").contains("95p размера ответа");
    }
}
