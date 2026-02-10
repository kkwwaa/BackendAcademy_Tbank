package academy.io.input;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public record FileSource(Path path) implements InputSource {
    public FileSource(Path path) {
        if (path == null) {
            throw new LogAnalyzerException(ErrorMessages.INVALID_FILEPATH + ": null", ExitCode.INVALID_USAGE.getCode());
        }
        this.path = path;
        validatePath();
    }

    private void validatePath() {
        if (!Files.exists(path)) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_FILEPATH + ": " + path, ExitCode.INVALID_USAGE.getCode());
        }
        if (!Files.isRegularFile(path)) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_FILEPATH + " (не является файлом): " + path,
                    ExitCode.INVALID_USAGE.getCode());
        }
        if (!Files.isReadable(path)) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_FILEPATH + " (нет прав на чтение): " + path,
                    ExitCode.INVALID_USAGE.getCode());
        }
    }

    @Override
    public Stream<String> lines() throws IOException {
        if (!Files.exists(path)) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_FILEPATH + ": " + path, ExitCode.INVALID_USAGE.getCode());
        }
        return Files.lines(path);
    }
}
