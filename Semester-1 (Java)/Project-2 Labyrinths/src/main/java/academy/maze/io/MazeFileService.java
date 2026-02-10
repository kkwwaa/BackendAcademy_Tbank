package academy.maze.io;

import academy.maze.dto.*;
import academy.maze.ui.enums.DisplayMode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Сервис для чтения и записи лабиринтов в файлы.
 *
 * <p>Работает с ASCII-представлением лабиринта.
 */
public class MazeFileService {

    public static Maze load(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Empty maze file");
        }

        int height = lines.size();
        int width = lines.get(0).length();
        Maze maze = new Maze(height, width);

        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            if (line.length() != width) {
                throw new IllegalArgumentException("Invalid row length at line " + (y + 1));
            }
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                CellType type = CellType.fromChar(c);
                maze.setCellType(new Point(y, x), type);
            }
        }
        return maze;
    }

    public static void save(Maze maze, String filename, DisplayMode displayMode) throws IOException {
        try (var writer = Files.newBufferedWriter(Path.of(filename))) {
            writer.write(maze.toString(displayMode));
        }
    }

    public static void saveForCLI(Maze maze, String filename) throws IOException {
        try (var writer = Files.newBufferedWriter(Path.of(filename))) {
            writer.write(maze.toStringForCLI());
        }
    }
}
