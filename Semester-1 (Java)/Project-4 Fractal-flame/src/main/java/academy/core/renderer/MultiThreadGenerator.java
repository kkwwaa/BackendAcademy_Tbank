package academy.core.renderer;

import academy.models.Config;
import academy.models.FractalImage;
import academy.models.Pixel;
import academy.utils.logger.LoggingProgressListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

/** Многопоточный генератор фрактала с локальными буферами. */
public final class MultiThreadGenerator extends Renderer {
    private static final int STEP = 10;

    private final int threadCount;

    private MultiThreadGenerator(int threadCount) {
        this.threadCount = threadCount;
    }

    public static FractalImage getFractalImage(
            @NotNull Config config, int threadCount, LoggingProgressListener listener) {
        MultiThreadGenerator generator = new MultiThreadGenerator(threadCount);
        return generator.getImage(config, listener);
    }

    @Override
    protected FractalImage getImage(Config config, LoggingProgressListener listener) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int iterationsPerThread = config.iterationCount() / threadCount;
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<FractalImage> localImages = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        // --- логируем старт ---
        listener.onStart(config.iterationCount());

        for (int t = 0; t < threadCount; t++) {
            FractalImage localImage = FractalImage.create(config.width(), config.height());
            localImages.add(localImage);

            executor.submit(() -> {
                try {
                    for (int i = 0; i < iterationsPerThread; i++) {
                        generate(config, localImage); // каждый поток пишет только в свой буфер

                        int done = counter.incrementAndGet();
                        // каждые STEP% прогресса
                        if (done % (config.iterationCount() / STEP) == 0) {
                            listener.onProgress(done, config.iterationCount());
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        // --- логируем завершение ---
        listener.onFinish(config.iterationCount());

        // Объединяем локальные изображения в одно итоговое
        FractalImage finalImage = FractalImage.create(config.width(), config.height());
        for (FractalImage local : localImages) {
            mergeImages(finalImage, local);
        }

        if (config.gammaCorrection()) {
            FractalImageCorrector.applyIntensityCorrection(finalImage, config.gamma());
        }
        return finalImage;
    }

    /** Оптимизированное объединение изображений */
    private static void mergeImages(FractalImage target, FractalImage source) {
        for (int y = 0; y < target.height(); y++) {
            for (int x = 0; x < target.width(); x++) {
                Pixel targetPixel = target.pixel(x, y);
                Pixel sourcePixel = source.pixel(x, y);

                if (sourcePixel.getHitCount() > 0) {
                    for (int i = 0; i < sourcePixel.getHitCount(); i++) {
                        targetPixel.incrementHitCount();
                        targetPixel.mixColor(sourcePixel.getRed(), sourcePixel.getGreen(), sourcePixel.getBlue());
                    }
                }
            }
        }
    }
}
