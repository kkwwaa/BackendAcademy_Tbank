package academy;

import academy.models.ClassInfo;
import academy.output.Formatter;
import academy.output.OutputFormat;
import academy.output.formatters.FormatterFactory;
import academy.services.InspectorService;
import academy.services.ObjectCreatorService;

public class ClassInspector {

    private static final InspectorService inspector = new InspectorService();
    private static final ObjectCreatorService creator = new ObjectCreatorService();

    public static String inspect(Class<?> clazz, OutputFormat format) {
        ClassInfo info = inspector.inspect(clazz);
        Formatter formatter = FormatterFactory.getFormatter(format);
        return formatter.format(info);
    }

    public static <T> T create(Class<T> clazz) {
        return creator.create(clazz);
    }
}
