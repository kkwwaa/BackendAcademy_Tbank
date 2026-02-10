package academy.maze.ui;

import academy.maze.dto.CellType;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;
import academy.maze.ui.input.InputProvider;
import academy.maze.ui.output.OutputProvider;
import lombok.AllArgsConstructor;

/**
 * Небольшой helper для ввода/валидации данных от пользователя: - ввод и проверка координат (точка внутри поля и не
 * стена) - ввод имени файла с нужным расширением
 *
 * <p>InputDialog не выполняет каких-либо действий над моделями — только спрашивает и валидирует ввод.
 */
@AllArgsConstructor
public class InputDialog {
    private final InputProvider input;
    private final OutputProvider output;

    /**
     * Считывает и валидирует точку, пока пользователь не введёт корректное значение.
     *
     * @param pointName имя точки для подсказки ("старта" / "финиша")
     * @param maze лабиринт (используется для границ и проверки, что точка — не стена)
     * @return валидная точка
     */
    public Point readPoint(String pointName, Maze maze) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        Point point = null;
        boolean valid = false;

        while (!valid) {
            output.print("Введите координаты " + pointName + " (x, y): ");
            String line = input.readLine();

            if (line == null) {
                output.print("Ввод прерван. Попробуйте снова.\n");
                continue;
            }

            line = line.trim();

            if (line.isEmpty()) {
                output.print("Пустой ввод. Введите координаты в формате 'x, y'.\n");
                continue;
            }

            try {
                point = Point.parse(line, false);
            } catch (IllegalArgumentException e) {
                output.print(e.getMessage() + "\n");
                continue;
            }

            if (point.y() < 0 || point.y() >= height || point.x() < 0 || point.x() >= width) {
                output.print(
                        "Точка за пределами лабиринта: (%d, %d), размер: %dx%d%n", point.x(), point.y(), width, height);
                continue;
            }

            if (maze.getCellType(point) == CellType.WALL) {
                output.print("Эта точка — стена. Выберите проход.\n");
                continue;
            }

            valid = true;
        }

        return point;
    }

    /**
     * Читает имя файла с проверкой на пустоту и требуемое расширение. Метод блокирует до тех пор, пока пользователь не
     * введёт корректное имя.
     *
     * @param extension ожидаемое расширение, например ".txt"
     * @return валидное имя файла
     */
    public String readFilenameWithExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            throw new IllegalArgumentException("extension must not be null or empty");
        }
        boolean valid = false;

        String filename = "";
        while (!valid) {
            output.print("Введите имя файла (например, solution" + extension + "): ");
            String line = input.readLine();

            if (line == null) {
                output.print("Ввод прерван. Попробуйте снова.\n");
                continue;
            }

            filename = line.trim();

            if (filename.isEmpty()) {
                output.print("Имя файла не может быть пустым.\n");
                continue;
            }

            if (!filename.endsWith(extension)) {
                output.print("Файл должен иметь расширение " + extension + ".\n");
                continue;
            }

            valid = true;
        }
        return filename;
    }
}
