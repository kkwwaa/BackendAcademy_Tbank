package academy.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

public final class LogConfig {

    public static final Logger LOG = LogManager.getLogger("LogAnalyzer");

    private LogConfig() {}

    public static void init() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        PatternLayout layout = PatternLayout.newBuilder()
                .withPattern("[%d{HH:mm:ss}] %-5level %msg%n")
                .withConfiguration(config)
                .build();

        ConsoleAppender consoleAppender = ConsoleAppender.newBuilder()
                .setName("Console")
                .setLayout(layout)
                .setTarget(ConsoleAppender.Target.SYSTEM_OUT)
                .build();
        consoleAppender.start();

        config.getRootLogger().addAppender(consoleAppender, null, null);
        ctx.updateLoggers();
    }
}
