package academy.models;

import academy.core.math.functions.Functions;
import java.util.List;

public record Config(
        int width, // ширина изображения
        int height, // высота изображения
        int iterationCount, // количество итераций
        int symmetry, // симметрия (лепестки)
        boolean gammaCorrection,
        double gamma, // гамма-коррекция
        Bounds bounds, // границы пространства
        long seed, // сид для воспроизводимости
        List<Functions> functions, // список вариаций (swirl, spherical и т.д.)
        AffineCoefficients[] coefficients, // массив аффинных матриц с цветами
        String outputPath,
        int threadsCount) {}
