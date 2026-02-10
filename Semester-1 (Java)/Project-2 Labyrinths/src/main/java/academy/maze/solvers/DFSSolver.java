package academy.maze.solvers;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.*;

/**
 * Решатель лабиринта с использованием алгоритма поиска в глубину (DFS).
 *
 * <p>Находит любой путь от старта до финиша. Не гарантирует оптимальности. Использует случайный порядок обхода соседей
 * для разнообразия путей.
 */
public class DFSSolver implements Solver {

    /** Источник случайных чисел для перемешивания соседей. */
    private final Random random = new Random();

    /**
     * Находит путь от стартовой точки до конечной.
     *
     * @param maze лабиринт
     * @param start стартовая точка
     * @param end конечная точка
     * @return путь (может быть пустым, если путь не найден)
     */
    @Override
    public Path solve(Maze maze, Point start, Point end) {
        if (!isValidEndpoints(maze, start, end)) {
            return new Path();
        }

        Set<Point> visited = new HashSet<>();
        Deque<Point> stack = new ArrayDeque<>();
        HashMap<Point, Point> cameFrom = new HashMap<>();

        stack.push(start);
        visited.add(start);
        cameFrom.put(start, null);

        while (!stack.isEmpty()) {
            Point current = stack.pop();

            if (current.equals(end)) {
                return new Path(current, cameFrom);
            }

            List<Point> neighbors = new ArrayList<>();
            getNeighbors(maze, current, neighbors);

            Collections.shuffle(neighbors, random);

            for (Point neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        return new Path();
    }

    @Override
    public String toString() {
        return "DFS (поиск в глубину)";
    }
}
