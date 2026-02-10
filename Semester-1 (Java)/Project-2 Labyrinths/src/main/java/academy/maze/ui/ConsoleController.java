package academy.maze.ui;

import academy.maze.core.MazeController;
import academy.maze.dto.Maze;
import academy.maze.dto.Point;
import academy.maze.generators.DFSGenerator;
import academy.maze.generators.kruskalGenerator.KruskalGenerator;
import academy.maze.generators.primGenerator.PrimGenerator;
import academy.maze.solvers.AStarSolver;
import academy.maze.solvers.DFSSolver;
import academy.maze.solvers.DijkstraSolver;
import academy.maze.ui.enums.Choice;
import academy.maze.ui.enums.DisplayMode;
import academy.maze.ui.input.InputProvider;
import academy.maze.ui.output.OutputProvider;

/** Интерактивный консольный интерфейс для работы с лабиринтами. */
public class ConsoleController {
    private final InputProvider input;
    private final OutputProvider output;
    private final MazeController controller;
    private final InputDialog inputDialog;

    private DisplayMode displayMode = DisplayMode.PLAIN;

    public ConsoleController(InputProvider input, OutputProvider output, MazeController controller) {
        this.input = input;
        this.output = output;
        this.controller = controller;
        this.inputDialog = new InputDialog(input, output);
    }

    public void start() {
        output.print(
                """
            Добро пожаловать в сервис генерации и решения лабиринтов различных размеров.
            Для генерации необходимо задать размеры,
            для решения — задать начальную и конечную точки.
            Начальный алгоритм генерации: алгоритм Прима.
            Начальный алгоритм решения: алгоритм A*.
            Символы UNICODE отключены.\n""");
        while (true) {
            Choice choice = showMenu();
            handleChoice(choice);
            if (choice == Choice.QUIT) {
                break;
            }
        }
    }

    private Choice showMenu() {
        output.print(
                """
            Выберите действие из доступных ниже (одно число):
            1) Сгенерировать лабиринт и сделать его текущим
            2) Напечатать текущий лабиринт
            3) Решить текущий лабиринт
            4) Сменить алгоритм генерации лабиринта
            5) Сменить алгоритм решения лабиринта""");
        if (displayMode == DisplayMode.PLAIN) {
            output.print("""
                6) Включить символы UNICODE
                7) Выйти""");
        } else {
            output.print("""
                6) Выключить символы UNICODE
                7) Выйти""");
        }
        return Choice.values()[input.readInt(Choice.values().length)];
    }

    private void handleChoice(Choice choice) {
        switch (choice) {
            case GENERATE -> generateMaze();
            case PRINT -> printCurrentMaze();
            case SOLVE -> solveMaze();
            case CHANGE_SOLVE -> changeSolver();
            case CHANGE_GENERATE -> changeGenerator();
            case CHANGE_SYMBOLS -> changeSymbols();
            case QUIT -> output.print("Пока!\n");
        }
    }

    private void generateMaze() {
        final int sizeLimitForSide = 1000;
        output.print("Введите количество строк:");
        int y = input.readInt(sizeLimitForSide) + 1;
        output.print("Введите количество столбцов:");
        int x = input.readInt(sizeLimitForSide) + 1;
        controller.generateMaze(y, x);
    }

    private void printCurrentMaze() {
        Maze maze = controller.getCurrentMaze();
        if (maze == null) {
            output.print("Лабиринт ещё не сгенерирован.\n");
            return;
        }
        output.printMaze(
                maze,
                "Лабиринт, сгенерированный алгоритмом "
                        + controller.getCurrentGenerator().toString(),
                displayMode);
    }

    private void solveMaze() {
        Maze maze = controller.getCurrentMaze();
        if (maze == null) {
            output.print("Сначала нужно сгенерировать лабиринт\n");
            return;
        }

        Point start = inputDialog.readPoint("старта", maze);
        Point end = inputDialog.readPoint("финиша", maze);

        controller.solveMaze(start, end);
        output.printMaze(
                controller.getCurrentMaze(),
                "Лабиринт, решенный алгоритмом " + controller.getCurrentSolver().toString(),
                displayMode);

        saveInFile();
    }

    private void changeSolver() {
        output.print(
                """
        Выберите алгоритм (одно число):
        1) алгоритм A* (A star)
        2) алгоритм Дейкстры
        3) алгоритм DFS""");
        final int cntSolveWay = 3;
        int choice = input.readInt(cntSolveWay);

        switch (choice) {
            case 0 -> controller.setCurrentSolver(new AStarSolver());
            case 1 -> controller.setCurrentSolver(new DijkstraSolver());
            case 2 -> controller.setCurrentSolver(new DFSSolver());
            default -> output.print("Некорректный выбор, попробуйте снова.");
        }
        output.print("Выбрана опция %d, алгоритм решения лабиринта изменён.%n", choice + 1);
    }

    private void changeGenerator() {
        output.print(
                """
            Выберите алгоритм (одно число):
            1) алгоритм Прима
            2) алгоритм DFS
            3) алгоритм Краскала""");
        final int cntGenerateWay = 3;
        int choice = input.readInt(cntGenerateWay);
        controller.setCurrentGenerator(
                switch (choice) {
                    case 0 -> new PrimGenerator();
                    case 1 -> new DFSGenerator();
                    case 2 -> new KruskalGenerator();
                    default -> throw new IllegalStateException();
                });
        output.print("Выбрана опция %d, алгоритм генерации лабиринта изменён.%n", choice + 1);
    }

    private void changeSymbols() {
        if (displayMode == DisplayMode.PLAIN) {
            displayMode = DisplayMode.UNICODE;
        } else {
            displayMode = DisplayMode.PLAIN;
        }
    }

    private void saveInFile() {
        output.print("""
    Хотите сохранить решение в файл? (одно число):
    1) Да
    2) Нет
    """);

        int choice = input.readInt(2);

        switch (choice) {
            case 0 -> {
                String filename = inputDialog.readFilenameWithExtension(".txt");
                if (controller.saveMaze(filename, displayMode)) {
                    output.print("Лабиринт успешно сохранён в файл: %s%n", filename);
                } else {
                    output.print("Ошибка при сохранении файла.\n");
                }
            }
            case 1 -> output.print("Решение не сохранено.");
            default -> output.print("Некорректный выбор, попробуйте снова.");
        }
    }
}
