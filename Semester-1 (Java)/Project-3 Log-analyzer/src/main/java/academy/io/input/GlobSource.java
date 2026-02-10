package academy.io.input;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/** Источник логов, заданный через glob-шаблон (например, logs/*.log). Раскрывает шаблон в список FileSource. */
public final class GlobSource {
    private final Path directory;
    private final String pattern;

    public GlobSource(String input) {
        String trimmed = input.trim();
        int sepIndex = Math.max(trimmed.lastIndexOf('/'), trimmed.lastIndexOf('\\'));
        if (sepIndex >= 0) {
            this.directory = Path.of(trimmed.substring(0, sepIndex));
            this.pattern = trimmed.substring(sepIndex + 1);
        } else {
            this.directory = Path.of(".");
            this.pattern = trimmed;
        }

        if (!Files.exists(this.directory) || !Files.isDirectory(this.directory) || !Files.isReadable(this.directory)) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_FILEPATH + ": " + this.directory, ExitCode.INVALID_USAGE.getCode());
        }
    }

    public List<FileSource> resolveFiles() {
        List<FileSource> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, pattern)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    String fileName = path.toString().toLowerCase();
                    if (!(fileName.endsWith(".log") || fileName.endsWith(".txt"))) {
                        throw new LogAnalyzerException(
                                ErrorMessages.UNSUPPORTED_FILE_EXTENSION + ": " + path,
                                ExitCode.INVALID_USAGE.getCode());
                    }
                    result.add(new FileSource(path));
                }
            }
        } catch (IOException exception) {
            throw new LogAnalyzerException(
                    ErrorMessages.FILE_READ_ERROR + ": " + directory, exception, ExitCode.INVALID_USAGE.getCode());
        }
        return result;
    }

    @Override
    public String toString() {
        return "GlobSource{" + directory + "/" + pattern + '}';
    }
}
