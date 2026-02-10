package academy.maze.core;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import academy.maze.generators.Generator;
import academy.maze.io.MazeFileService;
import academy.maze.ui.enums.DisplayMode;
import java.io.IOException;

public class MazeService {

    public Maze generate(Generator generator, int height, int width) {
        return generator.generate(height, width);
    }

    public Maze applyPath(Maze maze, Path path, Point start, Point end) {
        Maze withPath = maze.mazeWithPath(path);
        withPath.setCellType(start, CellType.START);
        withPath.setCellType(end, CellType.END);
        return withPath;
    }

    public boolean saveMaze(Maze maze, String filename, DisplayMode displayMode) {
        try {
            MazeFileService.save(maze, filename, displayMode);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
