package academy.cli;

import academy.ClassInspector;
import academy.config.ExitCode;
import academy.exception.ClassInspectorException;
import academy.exception.ErrorMessages;
import picocli.CommandLine;

@CommandLine.Command(name = "class-inspector", version = "1.0", mixinStandardHelpOptions = true)
public class CliService implements Runnable {

    @CommandLine.Mixin
    private Args args;

    @Override
    public void run() {
        ArgsValidator.validate(args);

        Class<?> clazz = loadClass(args.getClassName());
        String result = ClassInspector.inspect(clazz, args.getFormat());

        System.out.println(result);
    }

    public Class<?> loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (Exception exception) {
            throw new ClassInspectorException(ErrorMessages.CLASS_NOT_FOUND + ": " + name, ExitCode.INVALID_USAGE);
        }
    }
}
