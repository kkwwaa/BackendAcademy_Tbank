package academy.core.math.functions;

import academy.models.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HorseshoeFunction implements Functions {
    private double weight;

    @Override
    public Point apply(Point point) {
        double sqrtedRadius = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double newX = (point.x() * point.x() - point.y() * point.y()) / sqrtedRadius;
        double newY = 2 * point.x() * point.y() / sqrtedRadius;

        return new Point(newX, newY);
    }
}
