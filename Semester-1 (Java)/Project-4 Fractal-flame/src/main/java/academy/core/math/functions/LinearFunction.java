package academy.core.math.functions;

import academy.models.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinearFunction implements Functions {
    private double weight;

    @Override
    public Point apply(Point point) {
        return new Point(point.x(), point.y());
    }
}
