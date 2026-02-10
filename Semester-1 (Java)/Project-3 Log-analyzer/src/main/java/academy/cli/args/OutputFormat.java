package academy.cli.args;

import java.nio.file.Path;
import lombok.*;

@Getter
@RequiredArgsConstructor
public enum OutputFormat {
    JSON("json", ".json"),
    MARKDOWN("markdown", ".md"),
    ADOC("adoc", ".ad");

    private final String formatName;
    private final String extension;

    public boolean matchesFileName(Path output) {
        return output.toString().toLowerCase().endsWith(extension);
    }

    @Override
    public String toString() {
        return formatName;
    }
}
