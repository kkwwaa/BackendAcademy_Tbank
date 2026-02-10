package academy.cli.config;

import lombok.Getter;
import lombok.ToString;
import picocli.CommandLine.*;

//
@Command
@Getter
@ToString
public final class Args {

    @Option(
            names = {"-w", "--width"},
            description = "Ширина итогового изображения (по умолчанию: 1920)",
            defaultValue = "1920")
    private int width;

    @Option(
            names = {"-h", "--height"},
            description = "Высота итогового изображения (по умолчанию: 1080)",
            defaultValue = "1080")
    private int height;

    @Option(
            names = {"--seed"},
            description = "Начальное значение генератора (по умолчанию: 5)",
            defaultValue = "5")
    private long seed;

    @Option(
            names = {"-i", "--iteration-count"},
            description = "Количество итераций генерации (по умолчанию: 2500)",
            defaultValue = "2500")
    private int iterationCount;

    @Option(
            names = {"-o", "--output-path"},
            description = "Путь до PNG-файла результата (по умолчанию: result.png)",
            defaultValue = "result.png")
    private String outputPath;

    @Option(
            names = {"-t", "--threads"},
            description = "Количество потоков (по умолчанию: 1)",
            defaultValue = "1")
    private int threadsCount;

    @Option(
            names = {"-ap", "--affine-params"},
            description = "Аффинные преобразования в формате <a,b,c,d,e,f>/<...>. "
                    + "Пример: 0.1,0.1,0.1,0.1,0.1,0.1/0.2,0.3,0.0,0.1,0.2,0.0")
    private String affineParams;

    @Option(
            names = {"-f", "--functions"},
            description = "Функции трансформации с весами. "
                    + "Формат: <func:weight>,<func:weight>. Пример: swirl:1.0,horseshoe:0.8")
    private String functions;

    @Option(
            names = {"--config"},
            description = "Путь к JSON-конфигурации (необязательный)")
    private String configPath;

    @Option(
            names = {"-g", "--gamma-correction"},
            description = "Применить гамма-коррекцию (по умолчанию: false)",
            defaultValue = "false")
    private boolean gammaCorrection;

    @Option(
            names = {"--gamma"},
            description = "Значение гаммы для коррекции яркости (по умолчанию: 2.2)",
            defaultValue = "2.2")
    private double gamma;

    @Option(
            names = {"-s", "--symmetry-level"},
            description = "Уровень симметрии (по умолчанию: 1)",
            defaultValue = "1")
    private int symmetryLevel;
}
