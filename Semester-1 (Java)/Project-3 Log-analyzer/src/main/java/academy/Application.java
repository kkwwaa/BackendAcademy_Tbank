package academy;

import academy.cli.AnalyzerCommand;
import picocli.CommandLine;

public class Application {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new AnalyzerCommand())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
        System.exit(exitCode);
    }
}
