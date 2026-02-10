package academy.maze.solvers;

import academy.maze.dto.Point;

public record Node(Point pos, int currentPathCost, int totalCost) {
    public Node(Point pos, int currentPathCost) {
        this(pos, currentPathCost, currentPathCost);
    }
}
