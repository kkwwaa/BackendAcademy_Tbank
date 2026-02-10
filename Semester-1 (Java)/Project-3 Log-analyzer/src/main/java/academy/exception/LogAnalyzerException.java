package academy.exception;

import lombok.Getter;

/** Универсальное исключение для всех ошибок анализатора. Содержит код возврата для CLI. */
@Getter
public class LogAnalyzerException extends RuntimeException {
    private final int exitCode;

    public LogAnalyzerException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public LogAnalyzerException(String message, Throwable cause, int exitCode) {
        super(message, cause);
        this.exitCode = exitCode;
    }
}
