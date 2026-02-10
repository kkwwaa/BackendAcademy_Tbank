package academy.core.math.functions;

import academy.models.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwirlFunction implements Functions {
    private double weight;

    @Override
    public Point apply(Point point) {
        double sqrtedRadius = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double newX =
                point.x() * Math.sin(sqrtedRadius * sqrtedRadius) - point.y() * Math.cos(sqrtedRadius * sqrtedRadius);
        double newY =
                point.x() * Math.cos(sqrtedRadius * sqrtedRadius) + point.y() * Math.sin(sqrtedRadius * sqrtedRadius);
        return new Point(newX, newY);
    }
}
