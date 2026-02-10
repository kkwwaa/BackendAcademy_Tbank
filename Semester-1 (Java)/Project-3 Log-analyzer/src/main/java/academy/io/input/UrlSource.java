package academy.io.input;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public final class UrlSource implements InputSource {
    private final URL url;

    public UrlSource(String urlString) {
        try {
            this.url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_URL + ": " + urlString, ExitCode.INVALID_USAGE.getCode());
        }
    }

    @Override
    public Stream<String> lines() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return reader.lines().toList().stream();
        } catch (IOException exception) {
            throw new LogAnalyzerException(
                    ErrorMessages.ERROR_READING_LOGS + ": " + url, exception, ExitCode.INVALID_USAGE.getCode());
        }
    }
}
