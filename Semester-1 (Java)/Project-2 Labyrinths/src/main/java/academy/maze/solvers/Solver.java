package academy.maze.solvers;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.List;

/** Решатель лабиринта */
public interface Solver {

    /**
     * Решение лабиринта. Если путь не найден, то возвращается путь с длиной 0.
     *
     * @param maze лабиринт.
     * @param start начальная точка.
     * @param end конечная точка.
     * @return путь в лабиринте.
     */
    Path solve(Maze maze, Point start, Point end);

    default boolean isValidEndpoints(Maze maze, Point start, Point end) {
        return maze.getCellType(start) != CellType.WALL && maze.getCellType(end) != CellType.WALL;
    }

    default void getNeighbors(Maze maze, Point point, List<Point> neighbors) {
        int x = point.x();
        int y = point.y();

        Point up = new Point(y - 1, x);
        if (y - 1 >= 0 && maze.getCellType(up) == CellType.PASSAGE) {
            neighbors.add(up);
        }

        Point down = new Point(y + 1, x);
        if (y + 1 < maze.getHeight() && maze.getCellType(down) == CellType.PASSAGE) {
            neighbors.add(down);
        }

        Point left = new Point(y, x - 1);
        if (x - 1 >= 0 && maze.getCellType(left) == CellType.PASSAGE) {
            neighbors.add(left);
        }

        Point right = new Point(y, x + 1);
        if (x + 1 < maze.getWidth() && maze.getCellType(right) == CellType.PASSAGE) {
            neighbors.add(right);
        }
    }
}
