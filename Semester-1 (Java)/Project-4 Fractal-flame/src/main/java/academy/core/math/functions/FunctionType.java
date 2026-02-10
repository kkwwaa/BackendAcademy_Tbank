package academy.core.math.functions;

import academy.utils.exception.ErrorMessages;
import academy.utils.exception.FractalFlameException;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FunctionType {
    SWIRL("swirl", SwirlFunction::new),
    HORSESHOE("horseshoe", HorseshoeFunction::new),
    SPHERICAL("spherical", SphericalFunction::new),
    SINUSOIDAL("sinusoidal", SinusoidalFunction::new),
    LINEAR("linear", LinearFunction::new),
    HEART("heart", HeartFunction::new);

    private final String name;
    private final Supplier<Functions> constructor;

    public Functions create(double weight) {
        Functions function = constructor.get();
        function.setWeight(weight);
        return function;
    }

    public static FunctionType getFromName(String name) {
        for (FunctionType functionType : values()) {
            if (functionType.name.equalsIgnoreCase(name)) {
                return functionType;
            }
        }
        throw new FractalFlameException(ErrorMessages.UNSUPPORTED_FUNCTION + name);
    }
}
