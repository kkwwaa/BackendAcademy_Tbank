package academy.dto;

import static org.junit.jupiter.api.Assertions.*;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Maze")
class MazeTest {

    @Test
    @DisplayName("should initialize with all walls")
    void shouldInitializeWithWalls() {
        // Arrange
        Maze maze = new Maze(2, 2);

        // Act & Assert
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                assertEquals(CellType.WALL, maze.getCellType(new Point(y, x)));
            }
        }
    }

    @Test
    @DisplayName("should copy maze correctly")
    void shouldCopyMaze_WhenGetCopy() {
        // Arrange
        Maze original = new Maze(1, 1);
        original.setCellType(new Point(0, 0), CellType.START);

        // Act
        Maze copy = original.getCopy();

        // Assert
        assertNotSame(original, copy);
        assertEquals(CellType.START, copy.getCellType(new Point(0, 0)));
    }

    @Test
    @DisplayName("should apply path and keep start/end")
    void shouldApplyPath_AndKeepStartEnd() {
        // Arrange
        Maze maze = new Maze(1, 3);
        maze.setCellType(new Point(0, 0), CellType.START);
        maze.setCellType(new Point(0, 2), CellType.END);
        Path path = new Path();
        path.getPoints().add(new Point(0, 1));

        // Act
        Maze result = maze.mazeWithPath(path);

        // Assert
        assertEquals(CellType.START, result.getCellType(new Point(0, 0)));
        assertEquals(CellType.PATH, result.getCellType(new Point(0, 1)));
        assertEquals(CellType.END, result.getCellType(new Point(0, 2)));
    }

    @Test
    @DisplayName("should format ASCII with borders")
    void shouldFormatAscii_WithBorders() {
        // Arrange
        Maze maze = new Maze(1, 1);
        maze.setCellType(new Point(0, 0), CellType.PASSAGE);

        // Act
        String ascii = maze.toStringForCLI();

        // Assert
        String expected = """
                ###
                # #
                ###
                """
                .replace("\r\n", "\n");
        assertEquals(expected, ascii);
    }
}
