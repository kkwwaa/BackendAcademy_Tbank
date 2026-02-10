package academy.core.parser;

import academy.core.math.functions.FunctionType;
import academy.core.math.functions.Functions;
import java.util.ArrayList;
import java.util.List;

public class FunctionsParser {
    public static List<Functions> parse(String functions) {
        List<Functions> result = new ArrayList<>();

        if (functions == null || functions.isBlank()) {
            return result;
        }

        String[] items = functions.split(",");

        for (String item : items) {
            String[] parts = item.split(":");

            String name = parts[0].trim();
            double weight = Double.parseDouble(parts[1].trim());

            FunctionType type = FunctionType.getFromName(name);
            result.add(type.create(weight));
        }
        return result;
    }
}
