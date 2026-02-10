package academy.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import academy.parser.LogEntry;
import academy.report.ReportData;
import academy.report.utils.RequestPerDateStat;
import academy.report.utils.ResourceStat;
import academy.report.utils.ResponseCodeStat;
import academy.stats.LogStatistics;
import academy.util.DateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StatsCalculationTest {

    @Test
    @DisplayName("Полная проверка статистики")
    void fullStatsTest() {
        LogStatistics stats = new LogStatistics();

        // Логи в формате NGINX: dd/MMM/yyyy:HH:mm:ss Z
        stats.accept(new LogEntry(
                "1.1.1.1",
                "-",
                DateUtils.parseLogsDate("01/Jan/2025:10:00:00 +0000"),
                "GET",
                "/api",
                "HTTP/1.1",
                200,
                1000,
                "-",
                "bot"));
        stats.accept(new LogEntry(
                "2.2.2.2",
                "-",
                DateUtils.parseLogsDate("01/Jan/2025:11:00:00 +0000"),
                "GET",
                "/api",
                "HTTP/2.0",
                200,
                2000,
                "-",
                "bot"));
        stats.accept(new LogEntry(
                "1.1.1.1",
                "-",
                DateUtils.parseLogsDate("02/Jan/2025:12:00:00 +0000"),
                "POST",
                "/upload",
                "HTTP/1.1",
                201,
                5000,
                "-",
                "app"));

        ReportData report = stats.build();

        // Проверка общего количества запросов
        assertThat(report.totalRequestsCount()).isEqualTo(3);

        // Проверка среднего размера ответа
        assertThat(report.responseSizeInBytes().average()).isEqualTo(2666.67);

        // Проверка максимального размера ответа
        assertThat(report.responseSizeInBytes().max()).isEqualTo(5000);

        // Проверка 95-го перцентиля
        assertThat(report.responseSizeInBytes().p95()).isEqualTo(4700);

        // Проверка кодов ответа
        assertThat(report.responseCodes())
                .extracting(ResponseCodeStat::code, ResponseCodeStat::totalResponsesCount)
                .containsExactlyInAnyOrder(tuple(200, 2L), tuple(201, 1L));

        // Проверка ресурсов
        assertThat(report.resources())
                .extracting(ResourceStat::resource, ResourceStat::totalRequestsCount)
                .containsExactlyInAnyOrder(tuple("/api", 2L), tuple("/upload", 1L));

        // Проверка распределения по датам
        assertThat(report.requestsPerDate())
                .extracting(RequestPerDateStat::date, RequestPerDateStat::totalRequestsPercentage)
                .containsExactlyInAnyOrder(tuple("2025-01-01", 66.67), tuple("2025-01-02", 33.33));

        // Проверка уникальных протоколов
        assertThat(report.uniqueProtocols()).containsExactlyInAnyOrder("HTTP/1.1", "HTTP/2.0");

        // Проверка начального вида отчета (ReportData как DTO)
        assertThat(report).isNotNull();
        assertThat(report.totalRequestsCount()).isGreaterThan(0);
        assertThat(report.responseCodes()).isNotEmpty();
        assertThat(report.resources()).isNotEmpty();
        assertThat(report.uniqueProtocols()).isNotEmpty();
        assertThat(report.requestsPerDate()).isNotEmpty();
    }
}
