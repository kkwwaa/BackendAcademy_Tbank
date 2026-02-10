package academy.maze.generators;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;
import java.util.List;

/** Генератор лабиринта */
public interface Generator {

    /**
     * Генерирует лабиринт.
     *
     * @param width ширина лабиринта.
     * @param height высота лабиринта.
     * @return лабиринт
     * @throws IllegalArgumentException если невозможно сгенерировать лабиринт.
     */
    Maze generate(int width, int height);

    /**
     * Находит соседние клетки-стены (через одну клетку).
     *
     * @param maze лабиринт
     * @param point текущая точка
     * @param neighbors список для добавления соседей
     */
    default void getNeighbors(Maze maze, Point point, List<Point> neighbors) {
        int x = point.x();
        int y = point.y();

        Point up = new Point(y - 2, x);
        if (y - 2 >= 0 && maze.getCellType(up) == CellType.WALL && !neighbors.contains(up)) {
            neighbors.add(up);
        }
        Point down = new Point(y + 2, x);
        if (y + 2 < maze.getHeight() && maze.getCellType(down) == CellType.WALL && !neighbors.contains(down)) {
            neighbors.add(down);
        }
        Point left = new Point(y, x - 2);
        if (x - 2 >= 0 && maze.getCellType(left) == CellType.WALL && !neighbors.contains(left)) {
            neighbors.add(left);
        }
        Point right = new Point(y, x + 2);
        if (x + 2 < maze.getWidth() && maze.getCellType(right) == CellType.WALL && !neighbors.contains(right)) {
            neighbors.add(right);
        }
    }
}
