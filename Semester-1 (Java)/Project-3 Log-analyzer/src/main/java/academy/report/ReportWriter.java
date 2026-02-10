package academy.report;

import java.nio.file.Path;

/** Strategy интерфейс для записи отчётов. Содержит общий метод validateOutputFile для проверки файла. */
public interface ReportWriter {
    void write(ReportData report, Path output);
}
