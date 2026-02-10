package academy.models;

public record Bounds(double minX, double minY, double maxX, double maxY) {
    public boolean contains(Point point) {
        double x = point.x();
        double y = point.y();
        return x > minX && y > minY && x < maxX && y < maxY;
    }
}
