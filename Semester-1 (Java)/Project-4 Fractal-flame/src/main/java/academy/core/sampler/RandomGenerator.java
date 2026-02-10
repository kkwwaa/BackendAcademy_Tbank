package academy.core.sampler;

import java.util.Random;

public class RandomGenerator {
    private final Random random;

    public RandomGenerator(long seed) {
        this.random = new Random(seed);
    }

    public double nextDouble() {
        return random.nextDouble();
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public double nextInRange(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }
}
