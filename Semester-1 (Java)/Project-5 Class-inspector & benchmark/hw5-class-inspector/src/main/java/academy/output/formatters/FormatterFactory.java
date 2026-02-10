package academy.output.formatters;

import academy.output.Formatter;
import academy.output.OutputFormat;

public final class FormatterFactory {
    public static Formatter getFormatter(OutputFormat format) {
        return switch (format) {
            case JSON -> new JsonFormatter();
            case TEXT -> new TextFormatter();
        };
    }
}
