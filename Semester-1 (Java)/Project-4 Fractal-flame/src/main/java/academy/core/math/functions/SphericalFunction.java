package academy.core.math.functions;

import academy.models.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SphericalFunction implements Functions {
    private double weight;

    @Override
    public Point apply(Point point) {
        double sqrtedRadius = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point(point.x() / (sqrtedRadius * sqrtedRadius), point.y() / (sqrtedRadius * sqrtedRadius));
    }
}
