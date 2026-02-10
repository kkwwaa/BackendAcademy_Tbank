package academy.maze.solvers;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.*;

/**
 * Решатель лабиринта с использованием алгоритма A*.
 *
 * <p>Использует эвристику Манхэттенского расстояния для поиска кратчайшего пути.
 */
public class AStarSolver implements Solver {

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

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::totalCost));
        HashMap<Point, Integer> gScore = new HashMap<>();
        HashMap<Point, Point> cameFrom = new HashMap<>();

        gScore.put(start, 0);
        openSet.add(new Node(start, 0, getManhattanDistance(start, end)));

        while (!openSet.isEmpty()) {
            Node node = openSet.poll();

            if (node.pos().equals(end)) {
                return new Path(node.pos(), cameFrom);
            }

            List<Point> neighbors = new ArrayList<>();
            getNeighbors(maze, node.pos(), neighbors);

            for (Point neighbor : neighbors) {
                int tentativeG = gScore.get(node.pos()) + 1;

                if (tentativeG < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, node.pos());
                    gScore.put(neighbor, tentativeG);
                    int fScore = tentativeG + getManhattanDistance(neighbor, end);
                    openSet.add(new Node(neighbor, tentativeG, fScore));
                }
            }
        }
        return new Path();
    }

    /** Вычисляет Манхэттенское расстояние между точками. */
    private int getManhattanDistance(Point start, Point finish) {
        return Math.abs(start.y() - finish.y()) + Math.abs(start.x() - finish.x());
    }

    @Override
    public String toString() {
        return "A* (A star)";
    }
}
