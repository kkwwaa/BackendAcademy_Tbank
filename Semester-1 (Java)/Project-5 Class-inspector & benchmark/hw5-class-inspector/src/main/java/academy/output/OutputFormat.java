package academy.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutputFormat {
    TEXT("TEXT", ".txt"),
    JSON("JSON", ".json");

    private final String value;
    private final String extension;
}
