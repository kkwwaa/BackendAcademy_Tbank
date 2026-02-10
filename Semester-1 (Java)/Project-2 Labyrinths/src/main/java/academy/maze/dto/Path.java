package academy.maze.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import lombok.Getter;

/**
 * Путь в лабиринте. Точки в лабиринте находятся в порядке следования. Первой точкой является стартовая, последней —
 * финишная.
 */
@Getter
public class Path {
    private final LinkedList<Point> points = new LinkedList<>();

    public Path() {}

    public Path(Point end, HashMap<Point, Point> cameFrom) {
        Point current = end;
        while (current != null) {
            points.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(points);
    }

    public void markOnMaze(Maze maze) {
        for (Point point : points) {
            maze.setCellType(point, CellType.PATH);
        }
    }
}
