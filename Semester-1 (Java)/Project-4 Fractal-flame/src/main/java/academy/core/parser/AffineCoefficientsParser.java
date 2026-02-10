package academy.core.parser;

import java.util.ArrayList;
import java.util.List;

public class AffineCoefficientsParser {
    private static final int COEFFS_NUMBER = 6;

    public static List<List<Double>> parse(String affineParams) {
        List<List<Double>> matrices = new ArrayList<>();

        if (affineParams == null || affineParams.isBlank()) {
            return matrices;
        }

        String[] transforms = affineParams.split("/");

        for (String transform : transforms) {
            String[] parts = transform.split(",");

            List<Double> matrix = new ArrayList<>(COEFFS_NUMBER);
            for (int i = 0; i < COEFFS_NUMBER; i++) {
                matrix.add(Double.parseDouble(parts[i].trim()));
            }

            matrices.add(matrix);
        }

        return matrices;
    }
}
