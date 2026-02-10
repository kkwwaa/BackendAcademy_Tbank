package academy.cli;

import academy.parser.LogEntry;
import java.time.Instant;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;

/** Фильтр логов по диапазону дат. Реализован как Predicate<LogEntry>. */
@RequiredArgsConstructor
public final class LogFilter implements Predicate<LogEntry> {
    private final Instant fromDate;
    private final Instant toDate;

    public static LogFilter of(Instant fromDate, Instant toDate) {
        return new LogFilter(fromDate, toDate);
    }

    @Override
    public boolean test(LogEntry entry) {
        Instant timestamp = entry.timestamp();
        if (fromDate != null && timestamp.isBefore(fromDate)) {
            return false;
        }
        return toDate == null || !timestamp.isAfter(toDate);
    }
}
