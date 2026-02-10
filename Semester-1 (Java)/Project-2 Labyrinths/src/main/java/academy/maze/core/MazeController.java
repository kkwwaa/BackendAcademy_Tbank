package academy.maze.core;

import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.generators.Generator;
import academy.maze.solvers.Solver;
import academy.maze.ui.enums.DisplayMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MazeController {
    private Maze currentMaze;
    private Generator currentGenerator;
    private Solver currentSolver;

    private final MazeService service;

    public MazeController(MazeService service) {
        this.service = service;
    }

    public void generateMaze(int height, int width) {
        currentMaze = service.generate(currentGenerator, height, width);
    }

    public void solveMaze(Point start, Point end) {
        if (currentMaze == null) throw new IllegalStateException("Сначала сгенерируйте лабиринт");
        Path path = currentSolver.solve(currentMaze, start, end);
        currentMaze = service.applyPath(currentMaze, path, start, end);
    }

    public boolean saveMaze(String filename, DisplayMode displayMode) {
        return service.saveMaze(currentMaze, filename, displayMode);
    }
}
