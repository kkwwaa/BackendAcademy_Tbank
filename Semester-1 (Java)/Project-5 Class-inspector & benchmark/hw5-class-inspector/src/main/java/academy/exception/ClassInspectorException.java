package academy.exception;

import academy.config.ExitCode;
import lombok.Getter;

@Getter
public class ClassInspectorException extends RuntimeException {
    private final ExitCode exitCode;

    public ClassInspectorException(String message, ExitCode exitCode) {
        super(message);
        this.exitCode = exitCode;
    }
}
