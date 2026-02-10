package academy.report;

import academy.report.utils.RequestPerDateStat;
import academy.report.utils.ResourceStat;
import academy.report.utils.ResponseCodeStat;
import academy.report.utils.ResponseSize;
import java.util.List;

/**
 * DTO для итоговой статистики. Содержит все агрегированные данные, которые потом будут сериализованы в
 * JSON/Markdown/Adoc.
 */
public record ReportData(
        List<String> files,
        long totalRequestsCount,
        ResponseSize responseSizeInBytes,
        List<ResourceStat> resources,
        List<ResponseCodeStat> responseCodes,
        List<RequestPerDateStat> requestsPerDate,
        List<String> uniqueProtocols) {}
