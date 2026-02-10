package academy;

import academy.cli.CliCommand;
import picocli.CommandLine;

public class Application {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new CliCommand())
                .setUnmatchedArgumentsAllowed(true)
                .execute(args);
        System.exit(exitCode);
    }
}
