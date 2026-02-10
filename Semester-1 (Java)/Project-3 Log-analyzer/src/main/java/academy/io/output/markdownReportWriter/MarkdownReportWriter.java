package academy.io.output.markdownReportWriter;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.report.ReportData;
import academy.report.ReportWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Записывает отчет в формате Markdown по красивому шаблону с выравниванием. */
public final class MarkdownReportWriter implements ReportWriter {

    @Override
    public void write(ReportData report, Path output) {
        try {
            StringBuilder recordBody = new StringBuilder();

            // === Общая информация ===
            recordBody.append("#### Общая информация\n\n");
            List<String[]> metrics = List.of(
                    new String[] {"Файл(-ы)", "`" + String.join("`, `", report.files()) + "`"},
                    new String[] {"Количество запросов", String.valueOf(report.totalRequestsCount())},
                    new String[] {
                        "Средний размер ответа", report.responseSizeInBytes().average() + "b"
                    },
                    new String[] {
                        "Максимальный размер ответа",
                        report.responseSizeInBytes().max() + "b"
                    },
                    new String[] {
                        "95p размера ответа", report.responseSizeInBytes().p95() + "b"
                    });
            recordBody
                    .append(MarkdownReportWritersHelper.buildTable("Метрика", "Значение", metrics))
                    .append("\n\n");

            // === Запрашиваемые ресурсы ===
            recordBody.append("#### Запрашиваемые ресурсы\n\n");
            List<String[]> resources = report.resources().stream()
                    .map(resource -> new String[] {resource.resource(), String.valueOf(resource.totalRequestsCount())})
                    .toList();
            recordBody
                    .append(MarkdownReportWritersHelper.buildTable("Ресурс", "Количество", resources))
                    .append("\n\n");

            // === Коды ответа ===
            recordBody.append("#### Коды ответа\n\n");
            List<String[]> codes = report.responseCodes().stream()
                    .map(responseCodeStat -> new String[] {
                        String.valueOf(responseCodeStat.code()), String.valueOf(responseCodeStat.totalResponsesCount())
                    })
                    .toList();
            recordBody
                    .append(MarkdownReportWritersHelper.buildTable("Код", "Количество", codes))
                    .append("\n");

            Files.writeString(output, recordBody.toString());
        } catch (IOException exception) {
            throw new LogAnalyzerException(
                    ErrorMessages.FILE_WRITE_ERROR + ": " + output, exception, ExitCode.INVALID_USAGE.getCode());
        }
    }
}
