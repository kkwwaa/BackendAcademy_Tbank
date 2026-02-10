package academy.maze.ui.output;

import academy.maze.dto.Maze;
import academy.maze.ui.enums.DisplayMode;

public class ConsoleOutput implements OutputProvider {
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void print(String format, Object... args) {
        System.out.printf(format, args);
    }

    @Override
    public void printMaze(Maze maze, String prompt, DisplayMode mode) {
        System.out.println(prompt);
        System.out.println(maze.toString(mode));
    }
}
