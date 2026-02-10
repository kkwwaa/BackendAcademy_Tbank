package academy.cli.config;

import academy.core.math.functions.Functions;
import academy.models.AffineCoefficients;
import academy.models.Config;
import academy.utils.exception.ErrorMessages;
import academy.utils.exception.FractalFlameException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ConfigValidator {
    public static void validate(Config config) {
        validateDimensions(config.width(), config.height());
        validateIterations(config.iterationCount());
        validateThreadsCount(config.threadsCount());
        validateOutput(config.outputPath());
        validateAffineCoefficients(config.coefficients());
        validateFunctions(config.functions());
        validateSymmetry(config.symmetry());
    }

    private static void validateDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new FractalFlameException(ErrorMessages.INVALID_IMAGE_SIZE);
        }
    }

    private static void validateIterations(int iterations) {
        if (iterations <= 0) {
            throw new FractalFlameException(ErrorMessages.INVALID_ITERATION_COUNT);
        }
    }

    private static void validateThreadsCount(int threads) {
        if (threads <= 0) {
            throw new FractalFlameException(ErrorMessages.INVALID_THREADS);
        }
    }

    private static void validateOutput(String outputPath) {
        if (outputPath == null || outputPath.isBlank()) {
            throw new FractalFlameException(ErrorMessages.INVALID_FILEPATH + outputPath);
        }
        Path path = Path.of(outputPath);
        if (Files.exists(path)) {
            throw new FractalFlameException(ErrorMessages.FILE_ALREADY_EXISTS + outputPath);
        }
        if (!outputPath.endsWith(".png")) {
            throw new FractalFlameException(ErrorMessages.UNSUPPORTED_FILE_EXTENSION + outputPath);
        }
    }

    private static void validateSymmetry(int symmetryLevel) {
        if (symmetryLevel < 1) {
            throw new FractalFlameException(ErrorMessages.INVALID_SYMMETRY_LEVEL);
        }
    }

    /** Проверка списка функций */
    private static void validateFunctions(List<Functions> functions) {
        if (functions == null || functions.isEmpty()) {
            throw new FractalFlameException(ErrorMessages.UNSUPPORTED_FUNCTION + ": список пуст");
        }
        for (Functions function : functions) {
            if (function == null) {
                throw new FractalFlameException(ErrorMessages.UNSUPPORTED_FUNCTION + ": null");
            }
            if (function.getWeight() <= 0) {
                throw new FractalFlameException(ErrorMessages.INVALID_FUNCTIONS_FORMAT + ": вес <= 0");
            }
        }
    }

    /** Проверка массива аффинных коэффициентов */
    private static void validateAffineCoefficients(AffineCoefficients[] coefficients) {
        if (coefficients == null || coefficients.length == 0) {
            throw new FractalFlameException(ErrorMessages.INVALID_AFFINE_PARAMS + ": массив пуст");
        }
        for (AffineCoefficients coefficient : coefficients) {
            if (coefficient == null) {
                throw new FractalFlameException(ErrorMessages.INVALID_AFFINE_PARAMS + ": null");
            }
        }
    }
}
