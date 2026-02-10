package academy.maze.solvers;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.*;

/**
 * Решатель лабиринта с использованием алгоритма Дейкстры.
 *
 * <p>Находит кратчайший путь, рассматривая все ребра с весом 1.
 */
public class DijkstraSolver implements Solver {

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

        PriorityQueue<Node> nodesToSee = new PriorityQueue<>(Comparator.comparingInt(Node::currentPathCost));
        HashSet<Point> visited = new HashSet<>();
        HashMap<Point, Point> cameFrom = new HashMap<>();
        nodesToSee.add(new Node(start, 0));

        while (!nodesToSee.isEmpty()) {
            Node node = nodesToSee.poll();
            if (visited.contains(node.pos())) {
                continue;
            }
            if (node.pos().equals(end)) {
                return new Path(node.pos(), cameFrom);
            }
            visited.add(node.pos());

            List<Point> neighbors = new ArrayList<>();
            getNeighbors(maze, node.pos(), neighbors);
            for (Point neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    cameFrom.put(neighbor, node.pos());
                    nodesToSee.add(new Node(neighbor, node.currentPathCost() + 1));
                }
            }
        }
        return new Path();
    }

    @Override
    public String toString() {
        return "Дейкстра";
    }
}
