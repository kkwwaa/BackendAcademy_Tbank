package academy.maze.cli;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.io.MazeFileService;
import academy.maze.solvers.AStarSolver;
import academy.maze.solvers.DijkstraSolver;
import academy.maze.solvers.Solver;
import academy.maze.ui.enums.DisplayMode;
import java.io.IOException;
import picocli.CommandLine;

@CommandLine.Command(name = "solve", description = "Solve a maze with specified algorithm and points.")
public class SolveCommand implements Runnable {
    @CommandLine.Option(
            names = {"-a", "--algorithm"},
            required = true,
            description = "astar or dijkstra")
    private String algorithm;

    @CommandLine.Option(
            names = {"-f", "--file"},
            required = true)
    private String file;

    @CommandLine.Option(
            names = {"-s", "--start"},
            required = true,
            description = "Format: x,y")
    private String start;

    @CommandLine.Option(
            names = {"-e", "--end"},
            required = true,
            description = "Format: x,y")
    private String end;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Output file with solution")
    private String output;

    @Override
    public void run() {
        try {
            Maze maze = MazeFileService.load(file);

            Point startPoint = null;
            Point endPoint = null;

            try {
                startPoint = Point.parse(start);
                endPoint = Point.parse(end);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }

            if (startPoint.y() < 0
                    || startPoint.y() >= maze.getHeight()
                    || startPoint.x() < 0
                    || startPoint.x() >= maze.getWidth()) {
                System.out.println("Start point out of bounds: " + start);
                return;
            }
            if (endPoint.y() < 0
                    || endPoint.y() >= maze.getHeight()
                    || endPoint.x() < 0
                    || endPoint.x() >= maze.getWidth()) {
                System.out.println("End point out of bounds: " + end);
                return;
            }

            Solver solver =
                    switch (algorithm.toLowerCase()) {
                        case "astar" -> new AStarSolver();
                        case "dijkstra" -> new DijkstraSolver();
                        default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
                    };

            Path path = solver.solve(maze, startPoint, endPoint);
            if (path.getPoints().isEmpty()) {
                System.err.println("No path found");
                return;
            }

            Maze solvedMaze = maze.mazeWithPath(path);
            solvedMaze.setCellType(startPoint, CellType.START);
            solvedMaze.setCellType(endPoint, CellType.END);

            if (output != null) {
                MazeFileService.save(solvedMaze, output, DisplayMode.PLAIN);
            } else {
                System.out.print(solvedMaze);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
