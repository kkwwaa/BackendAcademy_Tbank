package academy.core.math.functions;

import academy.models.Point;
import java.util.function.Function;

public interface Functions extends Function<Point, Point> {
    double getWeight();

    void setWeight(double weight);
}
