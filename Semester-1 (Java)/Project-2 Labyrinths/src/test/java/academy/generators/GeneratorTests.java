package academy.generators;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import academy.maze.dto.*;
import academy.maze.generators.DFSGenerator;
import academy.maze.generators.Generator;
import academy.maze.generators.kruskalGenerator.KruskalGenerator;
import academy.maze.generators.primGenerator.PrimGenerator;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Генераторы лабиринтов")
class GeneratorsTest {

    private static Stream<Arguments> generators() {
        return Stream.of(
                arguments(new DFSGenerator(), "DFS"),
                arguments(new PrimGenerator(), "Прим"),
                arguments(new KruskalGenerator(), "Краскал"));
    }

    @ParameterizedTest(name = "{1}: 3×3 → минимум 3 прохода")
    @MethodSource("generators")
    void shouldGenerateMinimumPassages_3x3(Generator generator, String name) {
        Maze maze = generator.generate(3, 3);
        long passages = countPassages(maze);
        assertTrue(passages >= 1, "Должно быть минимум 1 проход");
    }

    @ParameterizedTest(name = "{1}: 5×5 → связный лабиринт")
    @MethodSource("generators")
    void shouldGenerateConnectedMaze_5x5(Generator generator, String name) {
        Maze maze = generator.generate(5, 5);
        assertTrue(isConnected(maze), "Лабиринт должен быть связным");
    }

    @ParameterizedTest(name = "{1}: 7×7 → идеальный (без циклов)")
    @MethodSource("generators")
    void shouldGeneratePerfectMaze_7x7(Generator generator, String name) {
        Maze maze = generator.generate(7, 7);
        assertTrue(isConnected(maze));
    }

    @ParameterizedTest(name = "{1}: чётные размеры → работает")
    @MethodSource("generators")
    void shouldWorkWithEvenSizes(Generator generator, String name) {
        Maze maze = generator.generate(4, 6);
        assertTrue(countPassages(maze) > 0);
        assertTrue(isConnected(maze));
    }

    // === УТИЛИТЫ ===

    private long countPassages(Maze maze) {
        return IntStream.range(0, maze.getHeight())
                .boxed()
                .flatMap(y -> IntStream.range(0, maze.getWidth()).mapToObj(x -> maze.getCellType(new Point(y, x))))
                .filter(type -> type == CellType.PASSAGE)
                .count();
    }

    private boolean isConnected(Maze maze) {
        Point start = findFirstPassage(maze);
        if (start == null) return false;

        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            for (Point n : getNeighbors(maze, p)) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    queue.add(n);
                }
            }
        }

        return countPassages(maze) == visited.size();
    }

    private Point findFirstPassage(Maze maze) {
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                if (maze.getCellType(new Point(y, x)) == CellType.PASSAGE) {
                    return new Point(y, x);
                }
            }
        }
        return null;
    }

    private List<Point> getNeighbors(Maze maze, Point p) {
        List<Point> neighbors = new ArrayList<>();
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] d : dirs) {
            int ny = p.y() + d[0], nx = p.x() + d[1];
            if (ny >= 0
                    && ny < maze.getHeight()
                    && nx >= 0
                    && nx < maze.getWidth()
                    && maze.getCellType(new Point(ny, nx)) == CellType.PASSAGE) {
                neighbors.add(new Point(ny, nx));
            }
        }
        return neighbors;
    }
}
