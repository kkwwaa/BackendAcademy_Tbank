package academy.output;

import academy.models.FractalImage;
import academy.models.Pixel;
import academy.utils.exception.ErrorMessages;
import academy.utils.exception.FractalFlameException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** ImageService — отвечает за конвертацию FractalImage в BufferedImage и сохранение результата в PNG с проверками. */
public final class ImageService {
    public static BufferedImage toBufferedImage(FractalImage fractalImage) {
        BufferedImage image =
                new BufferedImage(fractalImage.width(), fractalImage.height(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < fractalImage.height(); y++) {
            for (int x = 0; x < fractalImage.width(); x++) {
                Pixel pixel = fractalImage.pixel(x, y);
                int palette = (pixel.getRed() << 16) | (pixel.getGreen() << 8) | pixel.getBlue();
                image.setRGB(x, y, palette);
            }
        }
        return image;
    }

    public static void saveAsPng(BufferedImage image, File outputFile) throws IOException {
        if (image == null) {
            throw new FractalFlameException(ErrorMessages.INVALID_IMAGE);
        }
        if (outputFile == null) {
            throw new FractalFlameException(ErrorMessages.INVALID_FILEPATH);
        }
        if (!outputFile.getName().toLowerCase().endsWith(".png")) {
            throw new FractalFlameException(ErrorMessages.UNSUPPORTED_FILE_EXTENSION);
        }

        ImageIO.write(image, "png", outputFile);
    }

    public static void saveFractalAsPng(FractalImage fractalImage, File outputFile) throws IOException {
        BufferedImage image = toBufferedImage(fractalImage);
        saveAsPng(image, outputFile);
    }
}
