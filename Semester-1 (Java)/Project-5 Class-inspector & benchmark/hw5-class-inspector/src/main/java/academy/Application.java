package academy;

import academy.cli.CliService;
import academy.config.ExitCode;
import academy.exception.ClassInspectorException;
import academy.exception.ErrorMessages;
import picocli.CommandLine;

public class Application {

    public static void main(String[] args) {
        ExitCode exitCode = ExitCode.SUCCESS;

        try {
            new CommandLine(new CliService())
                    .setUnmatchedArgumentsAllowed(false)
                    .execute(args);

        } catch (ClassInspectorException exception) {
            // Ошибки пользователя: неверный класс, неверный формат и т.д.
            System.err.println(exception.getMessage());
            exitCode = exception.getExitCode();

        } catch (Exception exception) {
            // Непредвиденные ошибки: баги, NPE, ошибки сериализации и т.д.
            System.err.println(ErrorMessages.RUNTIME_ERROR + ": " + exception.getMessage());
            exception.printStackTrace(System.err);
            exitCode = ExitCode.UNEXPECTED_ERROR;
        }

        System.exit(exitCode.getCode());
    }
}
