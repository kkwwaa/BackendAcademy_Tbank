package academy.core.math;

import academy.core.math.functions.Functions;
import academy.models.AffineCoefficients;
import academy.models.Bounds;
import academy.models.Point;

public final class Transformer {
    /** Аффинное преобразование */
    public static Point applyAffine(Point point, AffineCoefficients affineCoefficients) {
        return affineCoefficients.transformPoint(point);
    }

    /** Нелинейная вариация */
    public static Point applyFunction(Point point, Functions function) {
        return function.apply(point);
    }

    /** Поворот на угол 2π/symmetry */
    public static Point rotate(Point point, int symmetry) {
        double angle = 2 * Math.PI / symmetry;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double newX = cos * point.x() - sin * point.y();
        double newY = sin * point.x() + cos * point.y();
        return new Point(newX, newY);
    }

    /** Нормализация в координаты изображения */
    public static Point zoom(Point point, Bounds bounds, int width, int height) {
        double normX = (point.x() - bounds.minX()) / (bounds.maxX() - bounds.minX());
        double normY = (point.y() - bounds.minY()) / (bounds.maxY() - bounds.minY());

        int pixelX = (int) (normX * (width - 1));
        int pixelY = (int) (normY * (height - 1));
        return new Point(pixelX, pixelY);
    }
}
