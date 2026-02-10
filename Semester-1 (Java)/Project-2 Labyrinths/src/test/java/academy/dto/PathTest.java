package academy.dto;

import static org.junit.jupiter.api.Assertions.*;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Path;
import academy.maze.dto.Point;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Path")
class PathTest {

    @Test
    @DisplayName("should mark all points as PATH")
    void shouldMarkAllPoints_AsPath() {
        // Arrange
        Maze maze = new Maze(1, 3);
        Path path = new Path();
        path.getPoints().addAll(List.of(new Point(0, 0), new Point(0, 1)));

        // Act
        path.markOnMaze(maze);

        // Assert
        assertEquals(CellType.PATH, maze.getCellType(new Point(0, 0)));
        assertEquals(CellType.PATH, maze.getCellType(new Point(0, 1)));
    }

    @Test
    @DisplayName("should reconstruct path from cameFrom map")
    void shouldReconstructPath_FromCameFrom() {
        // Arrange
        HashMap<Point, Point> cameFrom = new HashMap<>();
        cameFrom.put(new Point(0, 1), new Point(0, 0));
        cameFrom.put(new Point(0, 2), new Point(0, 1));

        // Act
        Path path = new Path(new Point(0, 2), cameFrom);

        // Assert
        List<Point> points = path.getPoints();
        assertEquals(3, points.size());
        assertEquals(new Point(0, 0), points.get(0));
        assertEquals(new Point(0, 2), points.get(2));
    }
}
