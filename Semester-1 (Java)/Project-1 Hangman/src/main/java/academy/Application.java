package academy;

import academy.model.*;
import academy.model.config.AppConfig;
import academy.model.config.DifficultyConfig;
import academy.model.enums.Difficulty;
import academy.service.HangmanEngine;
import academy.ui.ConsoleUI;
import academy.ui.HangmanDrawing;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.*;
import static java.util.Objects.nonNull;

@Command(name = "Hangman", version = "1.0", mixinStandardHelpOptions = true)
public class Application implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final ObjectReader YAML_READER = new ObjectMapper(new YAMLFactory()).findAndRegisterModules().reader();
    private static final Predicate<String[]> IS_TESTING_MODE = words -> nonNull(words) && words.length == 2;

    @Parameters(paramLabel = "<word>", description = "Words pair for testing mode")
    private String[] words;

    @Option(names = {"-c", "--config"}, description = "Path to YAML config file")
    private String configPath = "src/main/resources/config.yaml";

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        AppConfig config = loadConfig();
        HangmanEngine engine = new HangmanEngine(new Dictionary(config), new ConsoleUI(), new HangmanDrawing());
        if (IS_TESTING_MODE.test(words)) {
            LOGGER.atInfo().log("Non-interactive testing mode enabled");
            if (words[0].matches("[а-яА-Я]+") && words[1].matches("[а-яА-Я]*")) {
                String result = engine.playNonInteractive(words[0], words[1]);
                System.out.println(result);
            } else {
                System.out.println("Ошибка: используйте только русские буквы");
            }
        } else {
            LOGGER.atInfo().log("Interactive mode enabled");
            engine.playInteractive();
        }
    }

    private AppConfig loadConfig() {
        AppConfig config = new AppConfig();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.yaml")) {
            if (input == null) {
                LOGGER.atError().log("Файл config.yaml не найден в ресурсах");
                return config;
            }
            config = YAML_READER.readValue(input, AppConfig.class);
        } catch (IOException e) {
            LOGGER.atError().setCause(e).log("Ошибка при загрузке config.yaml, используется дефолт");
        }

        Difficulty.EASY.configure(config.getDifficulties().getOrDefault("EASY", new DifficultyConfig()));
        Difficulty.MEDIUM.configure(config.getDifficulties().getOrDefault("MEDIUM", new DifficultyConfig()));
        Difficulty.HARD.configure(config.getDifficulties().getOrDefault("HARD", new DifficultyConfig()));

        return config;
    }

}
