package academy;

import static org.junit.jupiter.api.Assertions.*;

import academy.maze.dto.*;
import academy.maze.io.MazeFileService;
import academy.maze.ui.enums.DisplayMode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("MazeFileService")
class MazeFileServiceTest {

    @Test
    @DisplayName("should load and save maze correctly")
    void shouldLoadAndSave(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path input = tempDir.resolve("in.txt");
        String content = """
                #O#
                # #
                #X#
                """
                .replace("\r\n", "\n");
        java.nio.file.Files.writeString(input, content);

        // Act
        Maze loaded = MazeFileService.load(input.toString());

        // Assert
        assertEquals(3, loaded.getHeight());
        assertEquals(CellType.START, loaded.getCellType(new Point(0, 1)));

        // Save
        Path output = tempDir.resolve("out.txt");
        MazeFileService.save(loaded, output.toString(), DisplayMode.PLAIN);

        String saved = java.nio.file.Files.readString(output);
        assertTrue(saved.contains("O"));
        assertTrue(saved.contains("X"));
    }

    @Test
    void load_nonexistentFile() {
        assertThrows(IOException.class, () -> MazeFileService.load("nonexistent.txt"));
    }

    @Test
    void load_emptyFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "");
        assertThrows(IllegalArgumentException.class, () -> MazeFileService.load(file.toString()));
    }

    @Test
    void load_invalidRowLength(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("bad.txt");
        Files.writeString(file, """
        ###
        ##
        ###
        """);
        assertThrows(IllegalArgumentException.class, () -> MazeFileService.load(file.toString()));
    }
}
