package academy;

import academy.maze.cli.GenerateCommand;
import academy.maze.cli.SolveCommand;
import academy.maze.core.MazeController;
import academy.maze.core.MazeService;
import academy.maze.generators.primGenerator.PrimGenerator;
import academy.maze.solvers.AStarSolver;
import academy.maze.ui.ConsoleController;
import academy.maze.ui.input.ConsoleInput;
import academy.maze.ui.output.ConsoleOutput;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "maze-app",
        description = "Maze generator and solver CLI application.",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {GenerateCommand.class, SolveCommand.class})
public class Application implements Runnable {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        var service = new MazeService();
        var controller = new MazeController(service);

        // ПО УМОЛЧАНИЮ
        controller.setCurrentGenerator(new PrimGenerator());
        controller.setCurrentSolver(new AStarSolver());

        var input = new ConsoleInput();
        var output = new ConsoleOutput();

        var view = new ConsoleController(input, output, controller);
        view.start();
    }
}
