package academy.io.input;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class LogSourceResolver {
    public static List<InputSource> resolve(List<String> paths) {
        List<InputSource> sources = new ArrayList<>();

        for (String input : paths) {
            if (input.startsWith("http://") || input.startsWith("https://")) {
                sources.add(new UrlSource(input.trim()));
            } else if (input.contains("*") || input.contains("?")) {
                GlobSource glob = new GlobSource(input.trim());
                sources.addAll(glob.resolveFiles());
            } else {
                Path filePath = Path.of(input.trim());
                if (!filePath.toString().endsWith(".log")
                        && !filePath.toString().endsWith(".txt")) {
                    throw new LogAnalyzerException(
                            ErrorMessages.UNSUPPORTED_FILE_EXTENSION + " : " + input, ExitCode.INVALID_USAGE.getCode());
                }
                sources.add(new FileSource(filePath));
            }
        }

        if (sources.isEmpty()) {
            throw new LogAnalyzerException(ErrorMessages.NO_LOG_FILES_FOUND, ExitCode.INVALID_USAGE.getCode());
        }

        return sources;
    }
}
