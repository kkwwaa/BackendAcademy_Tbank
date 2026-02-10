package academy.cli.args;

import academy.config.ExitCode;
import academy.exception.ErrorMessages;
import academy.exception.LogAnalyzerException;
import academy.util.DateUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class ArgsValidator {
    public static void validate(Args args) {
        List<String> paths = args.getPaths();
        if (paths == null || paths.isEmpty()) {
            throw new LogAnalyzerException(ErrorMessages.INVALID_FILEPATH, ExitCode.INVALID_USAGE.getCode());
        }

        for (String path : paths) {
            if (path == null || path.isBlank()) {
                throw new LogAnalyzerException(ErrorMessages.INVALID_FILEPATH, ExitCode.INVALID_USAGE.getCode());
            }
        }

        validateOutput(args.getOutput(), args.getFormat());
        validateDates(args.getFrom(), args.getTo());
    }

    private static void validateOutput(Path output, OutputFormat format) {
        if (output == null) {
            throw new LogAnalyzerException(ErrorMessages.FILE_WRITE_ERROR, ExitCode.INVALID_USAGE.getCode());
        }
        if (Files.exists(output)) {
            throw new LogAnalyzerException(
                    ErrorMessages.FILE_ALREADY_EXISTS + output, ExitCode.INVALID_USAGE.getCode());
        }
        if (!format.matchesFileName(output)) {
            throw new LogAnalyzerException(
                    ErrorMessages.MISMATCH_REPORT_FORMAT + " " + format.getFormatName(),
                    ExitCode.INVALID_USAGE.getCode());
        }
    }

    private static void validateDates(String from, String to) {
        Instant fromDate = null;
        Instant toDate = null;

        try {
            if (from != null) fromDate = DateUtils.parseArgsDate(from);
            if (to != null) toDate = DateUtils.parseArgsDate(to);
        } catch (DateTimeParseException e) {
            throw new LogAnalyzerException(ErrorMessages.INVALID_TIME_FORMAT, e, ExitCode.INVALID_USAGE.getCode());
        }

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new LogAnalyzerException(ErrorMessages.INVALID_DATE_RANGE, ExitCode.INVALID_USAGE.getCode());
        }
    }
}
