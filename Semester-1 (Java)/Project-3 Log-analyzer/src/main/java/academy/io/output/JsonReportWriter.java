package academy.io.output;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.report.ReportData;
import academy.report.ReportWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Path;

/** Записывает отчет в формате JSON. */
public final class JsonReportWriter implements ReportWriter {
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void write(ReportData report, Path output) {
        try {
            mapper.writeValue(output.toFile(), report);
        } catch (IOException exception) {
            throw new LogAnalyzerException(
                    ErrorMessages.FILE_WRITE_ERROR + ": " + output, exception, ExitCode.INVALID_USAGE.getCode());
        }
    }
}
