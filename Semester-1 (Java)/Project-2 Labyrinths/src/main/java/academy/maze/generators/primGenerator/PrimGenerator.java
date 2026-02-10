package academy.maze.generators.primGenerator;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;
import academy.maze.generators.Generator;
import java.util.*;

/**
 * Генератор лабиринта с использованием алгоритма Прима.
 *
 * <p>Строит лабиринт, начиная с одной клетки и случайно добавляя стены к существующим проходам.
 */
public class PrimGenerator implements Generator {

    /** Источник случайных чисел для выбора стартовой точки и направления. */
    private final Random random = new Random();

    /**
     * Генерирует лабиринт заданных размеров.
     *
     * @param height высота лабиринта
     * @param width ширина лабиринта
     * @return полностью сформированный лабиринт
     * @throws IllegalArgumentException если размеры недопустимы
     */
    @Override
    public Maze generate(int height, int width) {
        Maze maze = new Maze(height, width);
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        maze.setCellType(new Point(y, x), CellType.PASSAGE);
        List<Point> neighbors = new ArrayList<>();
        getNeighbors(maze, new Point(y, x), neighbors);

        while (!neighbors.isEmpty()) {
            int index = random.nextInt(neighbors.size());
            Point point = neighbors.remove(index);
            y = point.y();
            x = point.x();
            maze.setCellType(point, CellType.PASSAGE);

            List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
            while (!directions.isEmpty()) {
                Direction direction = directions.remove(random.nextInt(directions.size()));
                switch (direction) {
                    case UP:
                        if (y - 2 >= 0 && maze.getCellType(new Point(y - 2, x)) == CellType.PASSAGE) {
                            maze.setCellType(new Point(y - 1, x), CellType.PASSAGE);
                            directions.clear();
                        }
                        break;
                    case DOWN:
                        if (y + 2 < maze.getHeight() && maze.getCellType(new Point(y + 2, x)) == CellType.PASSAGE) {
                            maze.setCellType(new Point(y + 1, x), CellType.PASSAGE);
                            directions.clear();
                        }
                        break;
                    case LEFT:
                        if (x - 2 >= 0 && maze.getCellType(new Point(y, x - 2)) == CellType.PASSAGE) {
                            maze.setCellType(new Point(y, x - 1), CellType.PASSAGE);
                            directions.clear();
                        }
                        break;
                    case RIGHT:
                        if (x + 2 < maze.getWidth() && maze.getCellType(new Point(y, x + 2)) == CellType.PASSAGE) {
                            maze.setCellType(new Point(y, x + 1), CellType.PASSAGE);
                            directions.clear();
                        }
                        break;
                }
            }
            getNeighbors(maze, new Point(y, x), neighbors);
        }
        return maze;
    }

    @Override
    public String toString() {
        return "Прима";
    }
}
