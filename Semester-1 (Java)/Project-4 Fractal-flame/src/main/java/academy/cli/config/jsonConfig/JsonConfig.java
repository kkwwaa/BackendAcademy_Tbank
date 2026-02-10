package academy.cli.config.jsonConfig;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class JsonConfig {
    public static class Size {
        public int width = 1920;
        public int height = 1080;
    }

    @JsonProperty("size")
    public Size size = new Size();

    @JsonProperty("iteration_count")
    public int iterationCount = 2500;

    @JsonProperty("symmetry_level")
    public int symmetry = 1;

    @JsonProperty("gamma_correction")
    public boolean gammaCorrection = false;

    @JsonProperty("gamma")
    public double gamma = 2.2;

    @JsonProperty("output_path")
    public String outputPath = "result.png";

    @JsonProperty("threads")
    public int threads = 1;

    @JsonProperty("seed")
    public double seed = 5.0;

    @JsonProperty("functions")
    public List<FunctionDto> functions = List.of();

    @JsonProperty("affine_params")
    public List<AffineDto> affineParams = List.of();
}
