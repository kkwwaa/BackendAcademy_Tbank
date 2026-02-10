package academy.core.renderer;

import academy.models.FractalImage;
import academy.models.Pixel;

/**
 * FractalCorrection — постобработка изображения после рендера. Основные шаги: 1. Нормализация яркости (log scaling). 2.
 * Применение гамма-коррекции. 3. (Опционально) палитры и цветовые схемы.
 */
public final class FractalImageCorrector {
    /**
     * Применение коррекции интенсивности к изображению.
     *
     * @param image фрактальное изображение
     * @param gamma коэффициент гамма-коррекции (>0)
     */
    public static void applyIntensityCorrection(FractalImage image, double gamma) {
        // 1. Находим максимальное значение hitCount среди всех пикселей
        int maxHitCount = 0;
        for (Pixel pixel : image.pixels()) {
            if (pixel != null && pixel.getHitCount() > maxHitCount) {
                maxHitCount = pixel.getHitCount();
            }
        }

        if (maxHitCount == 0) {
            return; // изображение пустое, коррекция не нужна
        }

        // 2. Применяем логарифмическое масштабирование и гамма-коррекцию
        for (Pixel pixel : image.pixels()) {
            if (pixel == null || pixel.getHitCount() == 0) {
                continue;
            }

            // нормализованная интенсивность (0..1)
            double normalized = Math.log(pixel.getHitCount() + 1) / Math.log(maxHitCount + 1);

            // гамма-коррекция
            double corrected = Math.pow(normalized, 1.0 / gamma);

            // масштабируем цвет пикселя
            int red = (int) Math.min(255, pixel.getRed() * corrected);
            int green = (int) Math.min(255, pixel.getGreen() * corrected);
            int blue = (int) Math.min(255, pixel.getBlue() * corrected);

            // обновляем значения
            pixel.mixColor(red, green, blue);
        }
    }
}
