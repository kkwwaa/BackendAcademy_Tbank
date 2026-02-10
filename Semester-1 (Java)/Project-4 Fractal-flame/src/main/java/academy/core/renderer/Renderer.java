package academy.core.renderer;

import academy.core.math.Transformer;
import academy.core.math.functions.Functions;
import academy.core.sampler.Sampler;
import academy.models.AffineCoefficients;
import academy.models.Config;
import academy.models.FractalImage;
import academy.models.Pixel;
import academy.models.Point;
import academy.utils.logger.LoggingProgressListener;
import org.jetbrains.annotations.NotNull;

public abstract class Renderer implements Generator {
    private static final int BURN_IN_STEP = -20;

    /** Абстрактный метод: каждая реализация должна вернуть готовое изображение. */
    protected abstract FractalImage getImage(Config cfg, LoggingProgressListener listener);

    @Override
    public void generate(@NotNull Config config, FractalImage image) {
        Sampler sampler = new Sampler(config.seed(), config.functions(), config.coefficients());
        Point currentPoint = sampler.randomStart(config.bounds());

        for (int step = BURN_IN_STEP; step < config.iterationCount(); step++) {
            // выбор функции и коэффициентов
            Functions chosenFunction = sampler.pickFunction();
            AffineCoefficients chosenCoefficients = sampler.pickCoefficient();

            Point affinePoint = Transformer.applyAffine(currentPoint, chosenCoefficients);
            Point finalPoint = Transformer.applyFunction(affinePoint, chosenFunction);

            // симметрия
            for (int rotationIndex = 0; rotationIndex < config.symmetry(); rotationIndex++) {
                Point rotatedPoint = Transformer.rotate(finalPoint, config.symmetry());

                if (config.bounds().contains(rotatedPoint)) {
                    Point pixelCoordinates =
                            Transformer.zoom(rotatedPoint, config.bounds(), config.width(), config.height());
                    int pixelX = (int) pixelCoordinates.x();
                    int pixelY = (int) pixelCoordinates.y();

                    if (image.contains(pixelX, pixelY)) {
                        Pixel targetPixel = image.pixel(pixelX, pixelY);
                        chosenCoefficients.hitPixel(targetPixel);
                    }
                }
            }

            // обновление точки
            currentPoint = finalPoint;
        }

        // постобработка
        if (config.gammaCorrection()) {
            FractalImageCorrector.applyIntensityCorrection(image, config.gamma());
        }
    }
}
