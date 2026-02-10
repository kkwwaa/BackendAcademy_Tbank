package academy.maze.generators;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;
import java.util.*;

/**
 * Генератор лабиринта с использованием алгоритма DFS (поиск в глубину).
 *
 * <p>Создаёт идеальный (без циклов) лабиринт, проходящий рекурсивно по клеткам.
 */
public class DFSGenerator implements Generator {

    /** Источник случайных чисел для выбора стартовой точки и перемешивания соседей. */
    private final Random random = new Random();

    /**
     * Генерирует лабиринт заданных размеров.
     *
     * @param height высота лабиринта (чётное или нечётное — не критично)
     * @param width ширина лабиринта
     * @return полностью сформированный лабиринт
     * @throws IllegalArgumentException если размеры недопустимы
     */
    @Override
    public Maze generate(int height, int width) {
        Maze maze = new Maze(height, width);
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        dfs(maze, new Point(y, x));
        return maze;
    }

    /**
     * Рекурсивно вырезает проходы из стен.
     *
     * @param maze текущий лабиринт
     * @param point текущая точка (проход)
     */
    private void dfs(Maze maze, Point point) {
        List<Point> neighbors = new ArrayList<>();
        getNeighbors(maze, point, neighbors);
        maze.setCellType(point, CellType.PASSAGE);
        Collections.shuffle(neighbors, random);
        for (Point neighbor : neighbors) {
            int nx = neighbor.x();
            int ny = neighbor.y();
            if (maze.getCellType(new Point(ny, nx)) == CellType.WALL) {
                int wallX = (point.x() + nx) / 2;
                int wallY = (point.y() + ny) / 2;
                maze.setCellType(new Point(wallY, wallX), CellType.PASSAGE);
                dfs(maze, new Point(ny, nx));
            }
        }
    }

    @Override
    public String toString() {
        return "DFS";
    }
}
