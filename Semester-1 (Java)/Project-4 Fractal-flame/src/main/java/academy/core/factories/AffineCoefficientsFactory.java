package academy.core.factories;

import academy.core.sampler.RandomGenerator;
import academy.models.AffineCoefficients;
import java.util.List;

public class AffineCoefficientsFactory {
    public static AffineCoefficients[] createFromParams(
            List<List<Double>> parsedParams, int countIfEmpty, RandomGenerator random) {
        if (parsedParams == null || parsedParams.isEmpty()) {
            return generateRandom(countIfEmpty, random);
        }
        AffineCoefficients[] coefficients = new AffineCoefficients[parsedParams.size()];

        for (int i = 0; i < parsedParams.size(); i++) {
            List<Double> params = parsedParams.get(i);

            // Генерация случайного цвета
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);

            coefficients[i] = new AffineCoefficients(
                    params.get(0),
                    params.get(1),
                    params.get(2),
                    params.get(3),
                    params.get(4),
                    params.get(5),
                    red,
                    green,
                    blue);
        }

        return coefficients;
    }

    /** Генерация случайных аффинных преобразований. */
    private static AffineCoefficients[] generateRandom(int count, RandomGenerator random) {
        AffineCoefficients[] coefficients = new AffineCoefficients[count];

        for (int i = 0; i < count; i++) {
            // Случайные коэффициенты в диапазоне [-1, 1]
            double a = random.nextDouble() * 2 - 1;
            double b = random.nextDouble() * 2 - 1;
            double c = random.nextDouble() * 2 - 1;
            double d = random.nextDouble() * 2 - 1;
            double e = random.nextDouble() * 2 - 1;
            double f = random.nextDouble() * 2 - 1;

            // Случайный цвет
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);

            coefficients[i] = new AffineCoefficients(a, b, c, d, e, f, red, green, blue);
        }

        return coefficients;
    }
}
