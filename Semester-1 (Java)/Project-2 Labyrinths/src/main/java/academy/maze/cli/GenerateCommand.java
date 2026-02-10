package academy.maze.cli;

import academy.maze.dto.Maze;
import academy.maze.generators.DFSGenerator;
import academy.maze.generators.Generator;
import academy.maze.generators.primGenerator.PrimGenerator;
import academy.maze.io.MazeFileService;
import java.io.IOException;
import picocli.CommandLine;

@CommandLine.Command(name = "generate", description = "Generate a maze with specified algorithm and dimensions.")
public class GenerateCommand implements Runnable {
    @CommandLine.Option(
            names = {"-a", "--algorithm"},
            required = true,
            description = "dfs or prim")
    private String algorithm;

    @CommandLine.Option(
            names = {"-h", "--height"},
            required = true)
    private int height;

    @CommandLine.Option(
            names = {"-w", "--width"},
            required = true)
    private int width;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Output file")
    private String output;

    @Override
    public void run() {
        Generator generator =
                switch (algorithm.toLowerCase()) {
                    case "dfs" -> new DFSGenerator();
                    case "prim" -> new PrimGenerator();
                    default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
                };

        Maze maze = generator.generate(height, width);

        if (output != null) {
            try {
                MazeFileService.saveForCLI(maze, output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.print(maze.toStringForCLI());
        }
    }
}
