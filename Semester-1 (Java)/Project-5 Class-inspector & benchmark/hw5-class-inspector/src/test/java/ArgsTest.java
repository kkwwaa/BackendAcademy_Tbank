import static org.junit.jupiter.api.Assertions.*;

import academy.cli.Args;
import academy.cli.ArgsValidator;
import academy.cli.CliService;
import academy.config.ExitCode;
import academy.exception.ClassInspectorException;
import academy.output.OutputFormat;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class ArgsTest {

    @Test
    void shouldParseClassAndFormatCorrectly() {
        Args args = new Args();
        new CommandLine(args).parseArgs("--class", "java.util.ArrayList", "--format", "JSON");

        assertEquals("java.util.ArrayList", args.getClassName());
        assertEquals(OutputFormat.JSON, args.getFormat());
    }

    @Test
    void shouldUseDefaultFormatWhenNotProvided() {
        Args args = new Args();
        new CommandLine(args).parseArgs("--class", "java.lang.String");

        assertEquals("java.lang.String", args.getClassName());
        assertEquals(OutputFormat.TEXT, args.getFormat()); // defaultValue = TEXT
    }

    @Test
    void shouldValidateCorrectArguments() {
        Args args = new Args();
        new CommandLine(args).parseArgs("--class", "java.lang.String", "--format", "TEXT");

        assertDoesNotThrow(() -> ArgsValidator.validate(args));
    }

    @Test
    void shouldThrowWhenClassNotFound() {
        CliService cli = new CliService();
        ClassInspectorException ex = assertThrows(ClassInspectorException.class, () -> cli.loadClass("no.such.Class"));

        assertEquals(ExitCode.INVALID_USAGE, ex.getExitCode());
        assertTrue(ex.getMessage().contains("Класс не найден"));
    }

    @Test
    void picocliShouldFailOnInvalidFormat() {
        Args args = new Args();

        assertThrows(CommandLine.ParameterException.class, () -> new CommandLine(args)
                .parseArgs("--class", "java.lang.String", "--format", "XML"));
    }

    @Test
    void shouldThrowWhenFormatIsNull() {
        // создаём Args вручную без формата
        Args args = new Args();
        new CommandLine(args).parseArgs("--class", "java.lang.String");
        // вручную обнуляем формат
        // (picocli уже установил TEXT по умолчанию, поэтому сбрасываем)
        args = new Args(); // новый объект без парсинга → format = null

        Args finalArgs = args;
        ClassInspectorException ex =
                assertThrows(ClassInspectorException.class, () -> ArgsValidator.validate(finalArgs));

        assertEquals(ExitCode.INVALID_USAGE, ex.getExitCode());
    }
}
