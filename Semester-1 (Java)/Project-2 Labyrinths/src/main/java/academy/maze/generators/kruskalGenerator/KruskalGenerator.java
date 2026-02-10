package academy.maze.generators.kruskalGenerator;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;
import academy.maze.generators.Generator;
import java.util.*;

/**
 * Генератор лабиринта с использованием алгоритма Краскала.
 *
 * <p>Создаёт идеальный (без циклов) лабиринт, объединяя компоненты связности. Работает медленнее, чем Прима, но даёт
 * более "случайный" вид.
 */
public class KruskalGenerator implements Generator {

    /** Источник случайных чисел для перемешивания рёбер. */
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
        Map<Point, Integer> components = new HashMap<>();
        List<Edge> edges = new ArrayList<>();

        int componentId = 0;

        for (int y = 1; y < height; y += 2) {
            for (int x = 1; x < width; x += 2) {
                Point cell = new Point(y, x);
                maze.setCellType(cell, CellType.PASSAGE);
                components.put(cell, ++componentId);
            }
        }

        for (int y = 1; y < height; y += 2) {
            for (int x = 1; x < width; x += 2) {
                Point current = new Point(y, x);

                if (x + 2 < width) {
                    edges.add(new Edge(current, new Point(y, x + 2)));
                }
                if (y + 2 < height) {
                    edges.add(new Edge(current, new Point(y + 2, x)));
                }
            }
        }

        Collections.shuffle(edges, random);

        for (Edge edge : edges) {
            int componentFrom = components.get(edge.from());
            int componentTo = components.get(edge.to());

            if (componentFrom != componentTo) {
                int wallY = (edge.from().y() + edge.to().y()) / 2;
                int wallX = (edge.from().x() + edge.to().x()) / 2;
                maze.setCellType(new Point(wallY, wallX), CellType.PASSAGE);

                for (Map.Entry<Point, Integer> entry : components.entrySet()) {
                    if (entry.getValue() == componentTo) {
                        entry.setValue(componentFrom);
                    }
                }
            }
        }

        return maze;
    }

    @Override
    public String toString() {
        return "Краскал";
    }
}
