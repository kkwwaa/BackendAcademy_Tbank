package academy.core.renderer;

import academy.models.Config;
import academy.models.FractalImage;
import academy.utils.logger.LoggingProgressListener;
import org.jetbrains.annotations.NotNull;

/** Однопоточный генератор фрактала. */
public final class SingleThreadGenerator extends Renderer {

    private SingleThreadGenerator() {}

    public static FractalImage getFractalImage(@NotNull Config config, LoggingProgressListener listener) {
        SingleThreadGenerator generator = new SingleThreadGenerator();
        return generator.getImage(config, listener);
    }

    @Override
    protected FractalImage getImage(Config config, LoggingProgressListener listener) {
        listener.onStart(config.iterationCount());
        FractalImage image = FractalImage.create(config.width(), config.height());

        int step = Math.max(1, config.iterationCount() / 10); // каждые 10%
        for (int pass = 0; pass < config.iterationCount(); pass++) {
            generate(config, image);
            if (pass % step == 0) {
                listener.onProgress(pass, config.iterationCount());
            }
        }

        if (config.gammaCorrection()) {
            FractalImageCorrector.applyIntensityCorrection(image, config.gamma());
        }
        return image;
    }
}
