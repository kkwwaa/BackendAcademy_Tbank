package academy.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AffineCoefficients {
    // Геометрия
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;

    // Цвет
    private final int red;
    private final int green;
    private final int blue;

    /** Применение аффинного преобразования к точке */
    public Point transformPoint(Point point) {
        double newX = a * point.x() + b * point.y() + c;
        double newY = d * point.x() + e * point.y() + f;
        return new Point(newX, newY);
    }

    /** Нанесение цвета/яркости на пиксель */
    public void hitPixel(Pixel pixel) {
        pixel.incrementHitCount();
        pixel.mixColor(red, green, blue);
    }
}
