package academy.maze.dto;

/**
 * Координаты точки
 *
 * @param x
 * @param y
 */
public record Point(int y, int x) {
    public static Point parse(String str) {
        return parse(str, true);
    }

    public static Point parse(String str, boolean cli) {
        String[] parts = str.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid point format: " + str + ", expected format: x,y");
        }
        try {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            if (!cli) {
                return new Point(y - 1, x - 1);
            } else {
                return new Point(y, x);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates: " + str);
        }
    }
}
