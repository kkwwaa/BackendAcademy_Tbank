package academy.output.formatters;

import academy.models.ClassInfo;
import academy.models.FieldInfo;
import academy.models.MethodInfo;
import academy.output.Formatter;
import java.util.Map;

public class TextFormatter implements Formatter {

    @Override
    public String format(ClassInfo classInfo) {
        StringBuilder reportBody = new StringBuilder();

        reportBody.append("Class: ").append(classInfo.className()).append("\n");

        if (classInfo.superclass() != null) {
            reportBody.append("Superclass: ").append(classInfo.superclass()).append("\n");
        }

        if (!classInfo.interfaces().isEmpty()) {
            reportBody.append("Interfaces:\n");
            for (String currentInterface : classInfo.interfaces()) {
                reportBody.append("  - ").append(currentInterface).append("\n");
            }
        }

        if (!classInfo.fields().isEmpty()) {
            reportBody.append("Fields:\n");
            for (FieldInfo field : classInfo.fields()) {
                reportBody
                        .append("  - ")
                        .append(field.access())
                        .append(" ")
                        .append(field.name())
                        .append(" (")
                        .append(field.type())
                        .append(")\n");
            }
        }

        if (!classInfo.methods().isEmpty()) {
            reportBody.append("Methods:\n");
            for (MethodInfo method : classInfo.methods()) {
                reportBody
                        .append("  - ")
                        .append(method.access())
                        .append(" ")
                        .append(method.name())
                        .append("(")
                        .append(String.join(", ", method.params()))
                        .append(") : ")
                        .append(method.returnType())
                        .append("\n");
            }
        }

        if (!classInfo.annotations().isEmpty()) {
            reportBody.append("Annotations:\n");
            for (String annotation : classInfo.annotations()) {
                reportBody.append("  - ").append(annotation).append("\n");
            }
        }

        if (!classInfo.hierarchy().isEmpty()) {
            reportBody.append("Hierarchy:\n");
            formatHierarchy(reportBody, classInfo.hierarchy(), 1); // начинаем с 1го уровня
        }

        return reportBody.toString();
    }

    private void formatHierarchy(StringBuilder reportBody, Map<String, Object> hierarchy, int level) {
        for (Map.Entry<String, Object> entry : hierarchy.entrySet()) {
            reportBody.append("  ".repeat(level));
            if (level > 1) {
                reportBody.append("└── ");
            }
            reportBody.append(entry.getKey()).append("\n");

            if (entry.getValue() instanceof Map<?, ?> map) {
                formatHierarchy(reportBody, (Map<String, Object>) map, level + 1);
            }
        }
    }
}
