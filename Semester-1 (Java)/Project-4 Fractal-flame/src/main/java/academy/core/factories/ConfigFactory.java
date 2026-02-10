package academy.core.factories;

import academy.cli.config.jsonConfig.JsonConfig;
import academy.core.math.functions.Functions;
import academy.core.parser.AffineCoefficientsParser;
import academy.core.parser.FunctionsParser;
import academy.core.sampler.RandomGenerator;
import academy.models.AffineCoefficients;
import academy.models.Bounds;
import academy.models.Config;
import academy.utils.exception.ErrorMessages;
import academy.utils.exception.FractalFlameException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFactory {
    private static final int DEFAULT_AFFINE_COUNT = 3;
    private static final int DEFAULT_FUNCTION_COUNT = 3;

    public static Config create(
            int width,
            int height,
            int iterationCount,
            int symmetry,
            boolean gammaCorrection,
            double gamma,
            Bounds bounds,
            long seed,
            String functions,
            String affineParams,
            String outputPath,
            int threadsCount,
            RandomGenerator random) {
        AffineCoefficients[] affineCoefficients = AffineCoefficientsFactory.createFromParams(
                AffineCoefficientsParser.parse(affineParams), DEFAULT_AFFINE_COUNT, random);

        List<Functions> functionsList =
                FunctionsFactory.createFromParams(FunctionsParser.parse(functions), DEFAULT_FUNCTION_COUNT, random);

        return new Config(
                width,
                height,
                iterationCount,
                symmetry,
                gammaCorrection,
                gamma,
                bounds,
                seed,
                functionsList,
                affineCoefficients,
                outputPath,
                threadsCount);
    }

    public static Config createFromJson(File jsonFile, RandomGenerator random) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonConfig dto = mapper.readValue(jsonFile, JsonConfig.class);

            String functionsStr = dto.functions.stream()
                    .map(functionDto -> functionDto.name + ":" + functionDto.weight)
                    .collect(Collectors.joining(","));

            String affineStr = dto.affineParams.stream()
                    .map(affineDto -> affineDto.a + "," + affineDto.b + "," + affineDto.c + "," + affineDto.d + ","
                            + affineDto.e + "," + affineDto.f)
                    .collect(Collectors.joining("/"));

            // Парсим функции и аффинные коэффициенты через твои фабрики
            List<Functions> functionsList = FunctionsFactory.createFromParams(
                    FunctionsParser.parse(functionsStr), DEFAULT_FUNCTION_COUNT, random);

            AffineCoefficients[] affineCoefficients = AffineCoefficientsFactory.createFromParams(
                    AffineCoefficientsParser.parse(affineStr), DEFAULT_AFFINE_COUNT, random);

            return new Config(
                    dto.size.width,
                    dto.size.height,
                    dto.iterationCount,
                    dto.symmetry,
                    dto.gammaCorrection,
                    dto.gamma,
                    new Bounds(-2, -2, 2, 2),
                    (long) dto.seed,
                    functionsList,
                    affineCoefficients,
                    dto.outputPath,
                    dto.threads);
        } catch (IOException exception) {
            throw new FractalFlameException(ErrorMessages.CREATE_FORM_JSON_ERROR);
        }
    }
}
