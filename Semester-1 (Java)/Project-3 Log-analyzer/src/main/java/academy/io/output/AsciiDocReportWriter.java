package academy.io.output;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.report.ReportData;
import academy.report.ReportWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AsciiDocReportWriter implements ReportWriter {
    @Override
    public void write(ReportData report, Path output) {
        try {
            StringBuilder reportBody = new StringBuilder();
            reportBody
                    .append("== Общая информация\n\n")
                    .append("Файл(-ы): ")
                    .append(String.join(", ", report.files()))
                    .append("\n")
                    .append("* Количество запросов: ")
                    .append(report.totalRequestsCount())
                    .append("\n")
                    .append("* Средний размер ответа: ")
                    .append(report.responseSizeInBytes().average())
                    .append("b\n")
                    .append("* Максимальный размер ответа: ")
                    .append(report.responseSizeInBytes().max())
                    .append("b\n")
                    .append("* 95p размера ответа: ")
                    .append(report.responseSizeInBytes().p95())
                    .append("b\n\n");

            reportBody.append("== Коды ответа\n\n");
            report.responseCodes().forEach(code -> reportBody
                    .append("* ")
                    .append(code.code())
                    .append(" → ")
                    .append(code.totalResponsesCount())
                    .append("\n"));

            reportBody.append("\n== Запрашиваемые ресурсы\n\n");
            report.resources().forEach(res -> reportBody
                    .append("* ")
                    .append(res.resource())
                    .append(" → ")
                    .append(res.totalRequestsCount())
                    .append("\n"));

            reportBody.append("\n== Протоколы\n\n");
            report.uniqueProtocols()
                    .forEach(proto -> reportBody.append("* ").append(proto).append("\n"));

            reportBody.append("\n== Запросы по датам\n\n");
            report.requestsPerDate().forEach(rpd -> reportBody
                    .append("* ")
                    .append(rpd.date())
                    .append(" (")
                    .append(rpd.weekday())
                    .append(") → ")
                    .append(rpd.totalRequestsCount())
                    .append(" (")
                    .append(rpd.totalRequestsPercentage())
                    .append("%)\n"));

            Files.writeString(output, reportBody.toString());
        } catch (IOException exception) {
            throw new LogAnalyzerException(
                    ErrorMessages.FILE_WRITE_ERROR + ": " + output, exception, ExitCode.INVALID_USAGE.getCode());
        }
    }
}
