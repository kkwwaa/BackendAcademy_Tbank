package academy.io.output.markdownReportWriter;

import java.util.List;

/** Вспомогательные методы для построения Markdown-таблиц. */
public final class MarkdownReportWritersHelper {

    /** Строит таблицу с двумя колонками */
    public static String buildTable(String header1, String header2, List<String[]> rows) {
        int col1Width = Math.max(
                header1.length(),
                rows.stream().mapToInt(r -> r[0].length()).max().orElse(0));
        int col2Width = Math.max(
                header2.length(),
                rows.stream().mapToInt(r -> r[1].length()).max().orElse(0));

        StringBuilder tableBody = new StringBuilder();
        tableBody
                .append("| ")
                .append(padCenter(header1, col1Width))
                .append(" | ")
                .append(padRight(header2, col2Width))
                .append(" |\n");
        tableBody
                .append("|:")
                .append("-".repeat(col1Width))
                .append(":|")
                .append("-".repeat(col2Width))
                .append(":|\n");

        for (String[] row : rows) {
            tableBody
                    .append("| ")
                    .append(padCenter(row[0], col1Width))
                    .append(" | ")
                    .append(padRight(row[1], col2Width))
                    .append(" |\n");
        }
        return tableBody.toString();
    }

    private static String padRight(String value, int length) {
        return String.format("%-" + length + "s", value);
    }

    private static String padCenter(String value, int length) {
        int padding = length - value.length();
        int left = padding / 2;
        int right = padding - left;
        return " ".repeat(left) + value + " ".repeat(right);
    }
}
