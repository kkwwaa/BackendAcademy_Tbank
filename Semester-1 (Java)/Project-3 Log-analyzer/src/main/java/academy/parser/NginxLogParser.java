package academy.parser;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.util.DateUtils;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Парсер строк NGINX-лога. Pure Function: String → Optional<LogEntry>. */
public final class NginxLogParser {
    private static final Logger LOG = LogManager.getLogger(NginxLogParser.class);

    // Regex для формата:
    // 93.180.71.3 - - [17/May/2015:08:05:32 +0000] "GET /downloads/product_1 HTTP/1.1" 304 0 "-" "UserAgent"
    private static final Pattern LOG_PATTERN =
            Pattern.compile("^(?<ip>\\S+)\\s+-\\s+(?<user>\\S+)\\s+\\[(?<time>[^]]+)]\\s+"
                    + "\"(?<method>\\S+)\\s+(?<resource>\\S+)\\s+(?<protocol>\\S+)\"\\s+"
                    + "(?<status>\\d{3})\\s+(?<size>\\d+)\\s+"
                    + "\"(?<referer>[^\"]*)\"\\s+\"(?<agent>[^\"]*)\"$");

    private NginxLogParser() {}

    public static Optional<LogEntry> parse(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (!matcher.matches()) {
            LOG.warn(ErrorMessages.INVALID_FORMAT_NGINX + ": {}", line);
            return Optional.empty();
        }

        try {
            String ip = matcher.group("ip");
            String user = matcher.group("user");
            Instant timestamp = DateUtils.parseLogsDate(matcher.group("time"));
            String method = matcher.group("method");
            String resource = matcher.group("resource");
            String protocol = matcher.group("protocol");
            int status = Integer.parseInt(matcher.group("status"));
            long size = Long.parseLong(matcher.group("size"));
            String referer = matcher.group("referer");
            String agent = matcher.group("agent");

            return Optional.of(
                    new LogEntry(ip, user, timestamp, method, resource, protocol, status, size, referer, agent));
        } catch (Exception exception) {
            throw new LogAnalyzerException(
                    ErrorMessages.INVALID_FORMAT_NGINX + ": " + line, exception, ExitCode.INVALID_USAGE.getCode());
        }
    }
}
