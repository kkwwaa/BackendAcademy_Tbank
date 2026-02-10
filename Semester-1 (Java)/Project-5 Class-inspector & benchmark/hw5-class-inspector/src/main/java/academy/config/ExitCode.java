package academy.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Коды завершения программы. */
@Getter
@RequiredArgsConstructor
public enum ExitCode {
    SUCCESS(0),
    UNEXPECTED_ERROR(1),
    INVALID_USAGE(2);

    private final int code;
}
