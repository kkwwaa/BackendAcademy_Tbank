package academy.core.factories;

import academy.core.math.functions.FunctionType;
import academy.core.math.functions.Functions;
import academy.core.sampler.RandomGenerator;
import java.util.ArrayList;
import java.util.List;

public final class FunctionsFactory {
    private static final double MIN_WEIGHT = 0.1;
    private static final double MAX_WEIGHT = 1.0;

    /** Создание функций из парсинга CLI. Если список пустой → генерим случайные функции. */
    public static List<Functions> createFromParams(
            List<Functions> parsedFunctions, int countIfEmpty, RandomGenerator random) {
        if (parsedFunctions == null || parsedFunctions.isEmpty()) {
            return generateRandom(countIfEmpty, random);
        }
        return parsedFunctions;
    }

    /** Генерация случайных функций преобразования. */
    private static List<Functions> generateRandom(int count, RandomGenerator random) {
        List<Functions> functions = new ArrayList<>();
        FunctionType[] allTypes = FunctionType.values();

        for (int i = 0; i < count; i++) {
            // случайный тип из доступных
            FunctionType type = allTypes[random.nextInt(allTypes.length)];
            // случайный вес в диапазоне
            double weight = random.nextInRange(MIN_WEIGHT, MAX_WEIGHT);

            functions.add(type.create(weight));
        }
        return functions;
    }
}
