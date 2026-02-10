package academy.cli;

import academy.config.ExitCode;
import academy.exception.ClassInspectorException;
import academy.exception.ErrorMessages;
import academy.output.OutputFormat;

public final class ArgsValidator {

    private ArgsValidator() {}

    public static void validate(Args args) {
        validateOutput(args.getFormat());
    }

    private static void validateOutput(OutputFormat format) {
        if (format == null) {
            throw new ClassInspectorException(ErrorMessages.UNSUPPORTED_FORMAT, ExitCode.INVALID_USAGE);
        }
    }
}
