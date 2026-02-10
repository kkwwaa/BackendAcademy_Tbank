package academy.cli;

import academy.cli.config.Args;
import academy.cli.config.ConfigValidator;
import academy.core.factories.ConfigFactory;
import academy.core.renderer.FractalImageCorrector;
import academy.core.renderer.MultiThreadGenerator;
import academy.core.renderer.SingleThreadGenerator;
import academy.core.sampler.RandomGenerator;
import academy.models.Bounds;
import academy.models.Config;
import academy.models.FractalImage;
import academy.output.ImageService;
import academy.utils.exception.ErrorMessages;
import academy.utils.exception.FractalFlameException;
import academy.utils.logger.LoggingProgressListener;
import java.io.File;
import java.nio.file.Path;
import picocli.CommandLine;

public class CliCommand implements Runnable {
    @CommandLine.Mixin
    private Args args;

    private static final Bounds DEFAULT_BOUNDS = new Bounds(-2, -2, 2, 2);

    @Override
    public void run() {
        RandomGenerator random = new RandomGenerator(args.getSeed());

        Config config;
        if (args.getConfigPath() != null && !args.getConfigPath().isBlank()) {
            // Загружаем из JSON
            config = ConfigFactory.createFromJson(Path.of(args.getConfigPath()).toFile(), random);

        } else {
            // Сборка Config через фабрику
            config = ConfigFactory.create(
                    args.getWidth(),
                    args.getHeight(),
                    args.getIterationCount(),
                    args.getSymmetryLevel(),
                    args.isGammaCorrection(),
                    args.getGamma(),
                    DEFAULT_BOUNDS,
                    args.getSeed(),
                    args.getFunctions(),
                    args.getAffineParams(),
                    args.getOutputPath(),
                    args.getThreadsCount(),
                    random);
        }

        // Валидация
        ConfigValidator.validate(config);

        LoggingProgressListener listener = new LoggingProgressListener();

        FractalImage image;

        // --- Рендер ---
        if (config.threadsCount() == 1) {
            image = SingleThreadGenerator.getFractalImage(config, listener);
        } else {
            image = MultiThreadGenerator.getFractalImage(config, config.threadsCount(), listener);
        }

        // --- Постобработка ---
        FractalImageCorrector.applyIntensityCorrection(image, config.gamma());

        // --- Сохранение ---
        try {
            File outputFile = Path.of(config.outputPath()).toFile();
            ImageService.saveFractalAsPng(image, outputFile);
        } catch (Exception e) {
            throw new FractalFlameException(ErrorMessages.FILE_WRITE_ERROR);
        }
    }
}
