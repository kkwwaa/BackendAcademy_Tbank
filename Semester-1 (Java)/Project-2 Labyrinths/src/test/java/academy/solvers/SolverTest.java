package academy.solvers;

import static academy.maze.io.MazeFileService.load;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import academy.maze.dto.*;
import academy.maze.solvers.AStarSolver;
import academy.maze.solvers.DFSSolver;
import academy.maze.solvers.DijkstraSolver;
import academy.maze.solvers.Solver;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Решатели лабиринтов")
class SolversTest {

    private static Stream<Arguments> optimalSolvers() {
        return Stream.of(arguments(new AStarSolver(), "A*"), arguments(new DijkstraSolver(), "Dijkstra"));
    }

    private static Stream<Arguments> allSolvers() {
        return Stream.of(
                arguments(new AStarSolver(), "A*"),
                arguments(new DijkstraSolver(), "Dijkstra"),
                arguments(new DFSSolver(), "DFS"));
    }

    // === 1. ПРЯМОЙ ПУТЬ ===

    @ParameterizedTest(name = "{1}: прямой путь 1×3")
    @MethodSource("allSolvers")
    void shouldFindStraightPath_1x3(Solver solver, String name) {
        Maze maze = createStraightMaze();
        Point start = new Point(0, 0);
        Point end = new Point(0, 2);

        Path path = solver.solve(maze, start, end);

        assertFalse(path.getPoints().isEmpty(), name + ": путь должен быть найден");
        assertEquals(3, path.getPoints().size());
        assertTrue(path.getPoints().contains(new Point(0, 1)));
    }

    // === 2. ЗАБЛОКИРОВАННЫЙ ПУТЬ ===

    @ParameterizedTest(name = "{1}: нет пути — стена")
    @MethodSource("allSolvers")
    void shouldReturnEmpty_WhenBlocked(Solver solver, String name) {
        Maze maze = new Maze(1, 3);
        maze.setCellType(new Point(0, 0), CellType.PASSAGE);
        maze.setCellType(new Point(0, 1), CellType.WALL);
        maze.setCellType(new Point(0, 2), CellType.PASSAGE);

        Path path = solver.solve(maze, new Point(0, 0), new Point(0, 2));

        assertTrue(path.getPoints().isEmpty(), name + ": путь не должен быть найден");
    }

    // === 3. ОПТИМАЛЬНОСТЬ: A* = Dijkstra ===

    @ParameterizedTest(name = "{1}: оптимальный путь")
    @MethodSource("optimalSolvers")
    void shouldFindOptimalPath(Solver solver, String name) throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("complex_maze.txt");
        assertNotNull(resource, "Файл complex_maze.txt не найден в src/test/resources/");

        java.nio.file.Path loadPath = java.nio.file.Path.of(resource.toURI());

        Maze maze = load(loadPath.toString());

        Point start = findCell(maze, CellType.START);
        maze.setCellType(start, CellType.PASSAGE);
        Point end = findCell(maze, CellType.END);
        maze.setCellType(end, CellType.PASSAGE);

        Path path = solver.solve(maze, start, end);

        assertFalse(path.getPoints().isEmpty(), name + ": должен найти путь");
        assertEquals(20, path.getPoints().size() - 1, name + ": длина пути должна быть 20 шагов");
    }

    @Test
    @DisplayName("A* и Dijkstra: одинаковая длина пути")
    void aStarAndDijkstra_SameLength() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("complex_maze.txt");
        assertNotNull(resource, "Файл complex_maze.txt не найден в src/test/resources/");

        java.nio.file.Path loadPath = java.nio.file.Path.of(resource.toURI());

        Maze maze = load(loadPath.toString());

        Point start = findCell(maze, CellType.START);
        Point end = findCell(maze, CellType.END);

        int aStarSteps = new AStarSolver().solve(maze, start, end).getPoints().size() - 1;
        int dijkstraSteps =
                new DijkstraSolver().solve(maze, start, end).getPoints().size() - 1;

        assertEquals(dijkstraSteps, aStarSteps, "A* и Dijkstra должны найти путь одинаковой длины");
    }

    // === 4. DFS: любой путь, но не обязательно оптимальный ===

    @Test
    @DisplayName("DFS: находит путь, но не обязательно кратчайший")
    void dfs_FindsAnyPath() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("maze_multiple_paths.txt");
        assertNotNull(resource, "Файл maze_multiple_paths.txt не найден в src/test/resources/");

        java.nio.file.Path loadPath = java.nio.file.Path.of(resource.toURI());

        Maze maze = load(loadPath.toString());
        Point start = new Point(1, 1);
        Point end = new Point(3, 5);

        Path path = new DFSSolver().solve(maze, start, end);

        assertFalse(path.getPoints().isEmpty(), "DFS должен найти путь");
        assertTrue(path.getPoints().size() - 1 >= 5, "Путь не короче оптимального");
    }

    // === 5. СОХРАНЕНИЕ START/END ===

    @ParameterizedTest(name = "{1}: не меняет START/END")
    @MethodSource("allSolvers")
    void shouldPreserveStartEnd(Solver solver, String name) {
        Maze maze = new Maze(3, 3);
        Point start = new Point(0, 0);
        Point end = new Point(2, 2);
        maze.setCellType(start, CellType.START);
        maze.setCellType(end, CellType.END);

        solver.solve(maze, start, end);

        assertEquals(CellType.START, maze.getCellType(start));
        assertEquals(CellType.END, maze.getCellType(end));
    }

    // === 6. ГРАНИЧНЫЕ СЛУЧАИ ===

    @ParameterizedTest(name = "{1}: старт = финиш")
    @MethodSource("allSolvers")
    void startEqualsEnd(Solver solver, String name) {
        Maze maze = new Maze(1, 1);
        Point point = new Point(0, 0);
        maze.setCellType(point, CellType.PASSAGE);

        Path path = solver.solve(maze, point, point);

        assertEquals(1, path.getPoints().size());
        assertEquals(point, path.getPoints().get(0));
    }

    @ParameterizedTest(name = "{1}: старт — стена")
    @MethodSource("allSolvers")
    void startIsWall(Solver solver, String name) {
        Maze maze = new Maze(1, 1);
        Point point = new Point(0, 0);
        maze.setCellType(point, CellType.WALL);

        Path path = solver.solve(maze, point, point);

        assertTrue(path.getPoints().isEmpty());
    }

    // === 7. СЛОЖНЫЕ ЛАБИРИНТЫ ===

    @ParameterizedTest(name = "{1}: большой лабиринт")
    @MethodSource("optimalSolvers")
    void largeMaze(Solver solver, String name) throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("large_maze.txt");
        assertNotNull(resource, "Файл large_maze.txt не найден в src/test/resources/");

        java.nio.file.Path loadPath = java.nio.file.Path.of(resource.toURI());

        Maze maze = load(loadPath.toString());

        Point start = findCell(maze, CellType.START);
        maze.setCellType(start, CellType.PASSAGE);
        Point end = findCell(maze, CellType.END);
        maze.setCellType(end, CellType.PASSAGE);

        Path path = solver.solve(maze, start, end);

        assertFalse(path.getPoints().isEmpty(), name + ": должен найти путь в большом лабиринте");
    }

    // === УТИЛИТЫ ===

    private Maze createStraightMaze() {
        Maze maze = new Maze(1, 3);
        maze.setCellType(new Point(0, 0), CellType.PASSAGE);
        maze.setCellType(new Point(0, 1), CellType.PASSAGE);
        maze.setCellType(new Point(0, 2), CellType.PASSAGE);
        return maze;
    }

    private Point findCell(Maze maze, CellType type) {
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                if (maze.getCellType(new Point(y, x)) == type) {
                    return new Point(y, x);
                }
            }
        }
        throw new IllegalStateException("Не найдена ячейка типа " + type);
    }
}
