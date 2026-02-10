package academy.model;

import academy.model.config.DifficultyConfig;
import academy.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HangmanStateTest {
    private Difficulty difficulty;
    private Word word;

    @BeforeEach
    void setUp() {
        // Arrange: Настраиваем Difficulty и Word
        difficulty = Difficulty.EASY;
        DifficultyConfig config = new DifficultyConfig();
        config.setMaxMistakes(8);
        config.setMinWordLength(4);
        config.setMaxWordLength(6);
        difficulty.configure(config);

        word = new Word("мама");
    }

    @Test
    void testGuessLetterCorrect() {
        // Arrange
        HangmanState state = new HangmanState(word, difficulty);

        // Act
        boolean result = state.guessLetter('а');

        // Assert
        assertTrue(result, "Буква 'а' должна быть угадана");
        assertEquals("*а*а", state.getMask(), "Маска должна обновиться");
        assertEquals(0, state.getMistakesCount(), "Ошибок не должно быть");
        assertEquals(GameResult.IN_PROGRESS, state.getGameResult(), "Игра в процессе");
    }

    @Test
    void testGuessLetterIncorrect() {
        // Arrange
        HangmanState state = new HangmanState(word, difficulty);

        // Act
        boolean result = state.guessLetter('б');

        // Assert
        assertFalse(result, "Буква 'б' не должна быть угадана");
        assertEquals("****", state.getMask(), "Маска не должна измениться");
        assertEquals(1, state.getMistakesCount(), "Должна быть 1 ошибка");
        assertEquals(GameResult.IN_PROGRESS, state.getGameResult(), "Игра в процессе");
    }

    @Test
    void testGuessLetterCaseInsensitive() {
        // Arrange
        HangmanState state = new HangmanState(word, difficulty);

        // Act
        boolean resultUpper = state.guessLetter('А');
        boolean resultLower = state.guessLetter('м');

        // Assert
        assertTrue(resultUpper, "Верхний регистр 'А' должен работать");
        assertTrue(resultLower, "Нижний регистр 'м' должен работать");
        assertEquals("мама", state.getMask(), "Маска должна обновиться");
        assertEquals(0, state.getMistakesCount(), "Ошибок не должно быть");
    }

    @Test
    void testWinCondition() {
        // Arrange
        HangmanState state = new HangmanState(word, difficulty);

        // Act
        state.guessLetter('м');
        state.guessLetter('а');

        // Assert
        assertEquals("мама", state.getMask(), "Маска должна быть полной");
        assertEquals(GameResult.WIN, state.getGameResult(), "Должно быть WIN");
        assertEquals(0, state.getMistakesCount(), "Ошибок не должно быть");
    }

    @Test
    void testLoseCondition() {
        // Arrange
        HangmanState state = new HangmanState(word, difficulty);

        // Act
        for (int i = 0; i < 8; i++) {
            state.guessLetter('б');
        }

        // Assert
        assertEquals("****", state.getMask(), "Маска не должна измениться");
        assertEquals(8, state.getMistakesCount(), "Должно быть 8 ошибок");
        assertEquals(GameResult.LOSE, state.getGameResult(), "Должно быть LOSE");
    }

    @Test
    void testGetMaxMistakes() {
        // Arrange
        HangmanState state = new HangmanState(word, difficulty);

        // Assert
        assertEquals(8, state.getMaxMistakes(), "Максимум ошибок должен быть 8 для EASY");
    }
}
