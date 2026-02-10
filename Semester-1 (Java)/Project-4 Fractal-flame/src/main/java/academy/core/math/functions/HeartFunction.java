package academy.core.math.functions;

import academy.models.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeartFunction implements Functions {
    private double weight;

    @Override
    public Point apply(Point point) {
        double sqrtedRadius = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double theta = Math.atan2(point.y(), point.x());
        return new Point(sqrtedRadius * Math.sin(theta * sqrtedRadius), -sqrtedRadius * Math.cos(theta * sqrtedRadius));
    }
}
