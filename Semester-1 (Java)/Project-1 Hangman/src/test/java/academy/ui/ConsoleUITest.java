package academy.ui;

import academy.model.*;
import academy.model.config.DifficultyConfig;
import academy.model.enums.Difficulty;
import academy.model.enums.GameResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsoleUITest {
    @Mock
    private Scanner scanner;
    @Mock
    private PrintStream out;
    @Mock
    private HangmanDrawing hangmanDrawing;

    private ConsoleUI consoleUI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(out);
        consoleUI = new ConsoleUI() {
            @Override
            protected Scanner getScanner() {
                return scanner;
            }
        };
    }

    @Test
    void testChooseCategoryValidInput() {
        // Arrange
        List<String> categories = List.of("семья", "фрукты");
        when(scanner.nextLine()).thenReturn("1");

        // Act
        String result = consoleUI.chooseCategory(categories);

        // Assert
        assertEquals("семья", result, "Должна вернуть 'семья' для ввода '1'");
        verify(out).println("Выберите категорию:");
        verify(out).println("1. семья");
        verify(out).println("2. фрукты");
        verify(out).print("Введите номер (или нажмите Enter для случайного выбора): "); // Исправлено: println → print
        verify(scanner).nextLine();
    }

    @Test
    void testChooseCategoryRandomChoice() {
        // Arrange
        List<String> categories = List.of("семья", "фрукты");
        when(scanner.nextLine()).thenReturn("");

        // Act
        String result = consoleUI.chooseCategory(categories);

        // Assert
        assertTrue(categories.contains(result), "Должна вернуть 'семья' или 'фрукты'");
        verify(out).println("Выберите категорию:");
        verify(out).println("1. семья");
        verify(out).println("2. фрукты");
        verify(out).print("Введите номер (или нажмите Enter для случайного выбора): "); // Исправлено: println → print
        verify(out).println("Выбрано случайно: " + result);
        verify(scanner).nextLine();
    }

    @Test
    void testChooseCategoryInvalidInputThenValid() {
        // Arrange
        List<String> categories = List.of("семья", "фрукты");
        when(scanner.nextLine()).thenReturn("3", "abc", "1");

        // Act
        String result = consoleUI.chooseCategory(categories);

        // Assert
        assertEquals("семья", result, "Должна вернуть 'семья' после некорректного ввода");
        verify(out).println("Выберите категорию:");
        verify(out).println("1. семья");
        verify(out).println("2. фрукты");
        verify(out, times(3)).print("Введите номер (или нажмите Enter для случайного выбора): "); // Исправлено: println → print
        verify(out).println("Ошибка: выберите номер от 1 до 2");
        verify(out).println("Ошибка: введите число");
        verify(scanner, times(3)).nextLine();
    }

    @Test
    void testChooseDifficultyValidInput() {
        // Arrange
        when(scanner.nextLine()).thenReturn("1");
        DifficultyConfig config = new DifficultyConfig();
        config.setMaxMistakes(8);
        config.setMinWordLength(4);
        config.setMaxWordLength(6);
        Difficulty.EASY.configure(config);

        // Act
        Difficulty result = consoleUI.chooseDifficulty();

        // Assert
        assertEquals(Difficulty.EASY, result, "Должна вернуть EASY");
        verify(out).println("Выберите сложность:");
        verify(out).println("1. EASY (8 ошибок, длина слова от 4 до 6 символов)");
        verify(out).print("Введите номер (или нажмите Enter для случайного выбора): "); // Исправлено: println → print
        verify(scanner).nextLine();
    }

    @Test
    void testPrintMask() {
        // Arrange
        HangmanState state = mock(HangmanState.class);
        when(state.getMask()).thenReturn("м**а");

        // Act
        consoleUI.printMask(state);

        // Assert
        verify(out).println("Слово: м**а");
    }

    @Test
    void testPrintStateIncorrectGuess() {
        // Arrange
        HangmanState state = mock(HangmanState.class);
        when(state.getMistakesCount()).thenReturn(2);
        when(state.getMaxMistakes()).thenReturn(8);
        when(state.getMask()).thenReturn("м**а");
        when(hangmanDrawing.getStage(2, 8)).thenReturn(" O|\n |\n");

        // Act
        consoleUI.printState(state, hangmanDrawing, false);

        // Assert
        verify(out).println("Слово: м**а");
        verify(out).println("Виселица:\n O|\n |\n");
        verify(out).println("Осталось попыток: 6");
    }

    @Test
    void testPrintResultWin() {
        // Arrange
        HangmanState state = mock(HangmanState.class);
        when(state.getGameResult()).thenReturn(GameResult.WIN);
        when(state.getMask()).thenReturn("мама");

        // Act
        consoleUI.printResult(state);

        // Assert
        verify(out).println("Слово: мама");
        verify(out).println("Вы выиграли!");
    }

    @Test
    void testPrintResultLose() {
        // Arrange
        HangmanState state = mock(HangmanState.class);
        when(state.getGameResult()).thenReturn(GameResult.LOSE);
        when(state.getMask()).thenReturn("м**а");

        // Act
        consoleUI.printResult(state);

        // Assert
        verify(out).println("Слово: м**а");
        verify(out).println("Вы проиграли(");
    }

    @Test
    void testGetLetterValidInput() {
        // Arrange
        when(scanner.nextLine()).thenReturn("а");

        // Act
        char result = consoleUI.getLetter("Подсказка");

        // Assert
        assertEquals('а', result, "Должна вернуть 'а'");
        verify(out).print("Введите букву (или 'h' для подсказки): ");
        verify(scanner).nextLine();
    }

    @Test
    void testGetLetterHintUsedOnce() {
        // Arrange
        when(scanner.nextLine()).thenReturn("h", "а");

        // Act
        char result = consoleUI.getLetter("Подсказка");

        // Assert
        assertEquals('а', result, "Должна вернуть 'а' после подсказки");
        verify(out).println("Подсказка: Подсказка");
        verify(out, times(2)).print("Введите букву (или 'h' для подсказки): ");
        verify(scanner, times(2)).nextLine();
    }

    @Test
    void testGetLetterHintUsedTwice() {
        // Arrange
        when(scanner.nextLine()).thenReturn("h", "h", "а");

        // Act
        char result = consoleUI.getLetter("Подсказка");

        // Assert
        assertEquals('а', result, "Должна вернуть 'а' после второй подсказки");
        verify(out).println("Подсказка: Подсказка");
        verify(out).println("Подсказка уже использована!");
        verify(out, times(3)).print("Введите букву (или 'h' для подсказки): ");
        verify(scanner, times(3)).nextLine();
    }

    @Test
    void testGetLetterInvalidInput() {
        // Arrange
        when(scanner.nextLine()).thenReturn("аб", "1", "а");

        // Act
        char result = consoleUI.getLetter("Подсказка");

        // Assert
        assertEquals('а', result, "Должна вернуть 'а' после некорректного ввода");
        verify(out, times(2)).println("Ошибка: введите одну русскую букву!");
        verify(out, times(3)).print("Введите букву (или 'h' для подсказки): ");
        verify(scanner, times(3)).nextLine();
    }
}
