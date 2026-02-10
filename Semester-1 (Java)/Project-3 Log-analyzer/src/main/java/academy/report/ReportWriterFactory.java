package academy.report;

import academy.cli.args.OutputFormat;
import academy.io.output.AsciiDocReportWriter;
import academy.io.output.JsonReportWriter;
import academy.io.output.markdownReportWriter.MarkdownReportWriter;

public final class ReportWriterFactory {
    private ReportWriterFactory() {}

    public static ReportWriter forFormat(OutputFormat format) {
        return switch (format) {
            case JSON -> new JsonReportWriter();
            case MARKDOWN -> new MarkdownReportWriter();
            case ADOC -> new AsciiDocReportWriter();
        };
    }
}
