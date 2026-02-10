package academy.core.renderer;

import academy.models.Config;
import academy.models.FractalImage;
import org.jetbrains.annotations.NotNull;

public interface Generator {
    void generate(@NotNull Config config, FractalImage image);
}
