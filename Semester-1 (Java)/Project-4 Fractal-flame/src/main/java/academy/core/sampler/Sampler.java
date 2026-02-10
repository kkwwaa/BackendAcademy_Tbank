package academy.core.sampler;

import academy.core.math.functions.Functions;
import academy.models.AffineCoefficients;
import academy.models.Bounds;
import academy.models.Point;
import java.util.List;

public final class Sampler {
    private final RandomGenerator random;
    private final List<Functions> functions;
    private final double[] weightedDistribution;
    private final AffineCoefficients[] coefficients;

    public Sampler(long seed, List<Functions> functions, AffineCoefficients[] coefficients) {
        this.random = new RandomGenerator(seed);
        this.functions = functions;
        this.coefficients = coefficients;
        this.weightedDistribution = buildWeightedDistribution(functions);
    }

    /** Генерация случайной стартовой точки внутри bounds. */
    public Point randomStart(Bounds bounds) {
        double x = random.nextInRange(bounds.minX(), bounds.maxX());
        double y = random.nextInRange(bounds.minY(), bounds.maxY());
        return new Point(x, y);
    }

    /** Выбор функции по весам (PDF/CDF). */
    public Functions pickFunction() {
        double randomChoice = random.nextDouble(); // случайное число от 0 до 1
        for (int i = 0; i < weightedDistribution.length; i++) {
            if (randomChoice <= weightedDistribution[i]) {
                return functions.get(i);
            }
        }
        return functions.getLast();
    }

    /** Равномерный выбор коэффициентов. */
    public AffineCoefficients pickCoefficient() {
        int index = random.nextInt(coefficients.length);
        return coefficients[index];
    }

    /** Построение CDF из весов функций. */
    private double[] buildWeightedDistribution(List<Functions> functions) {
        double totalWeight =
                functions.stream().mapToDouble(Functions::getWeight).sum();

        double[] weightedDistributions = new double[functions.size()];
        double cumulative = 0.0;
        for (int i = 0; i < functions.size(); i++) {
            cumulative += functions.get(i).getWeight() / totalWeight;
            weightedDistributions[i] = cumulative;
        }
        return weightedDistributions;
    }
}
