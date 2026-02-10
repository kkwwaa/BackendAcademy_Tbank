package academy.maze.ui.output;

import academy.maze.dto.Maze;
import academy.maze.ui.enums.DisplayMode;

public interface OutputProvider {
    void print(String message);

    void print(String format, Object... args);

    void printMaze(Maze maze, String prompt, DisplayMode mode);
}
