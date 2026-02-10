package academy.core.math.functions;

import academy.models.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SinusoidalFunction implements Functions {
    private double weight;

    @Override
    public Point apply(Point point) {
        return new Point(Math.sin(point.x()), Math.sin(point.y()));
    }
}
