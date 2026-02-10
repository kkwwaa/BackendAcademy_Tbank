package academy.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    @Test
    void testConstructorValidWord() {
        // Arrange
        String input = "МаМа";

        // Act
        Word word = new Word(input);

        // Assert
        assertEquals("****", word.getMask(), "Маска должна быть из звёздочек");
        assertFalse(word.isFullyGuessed(), "Слово не угадано изначально");
    }

    @Test
    void testConstructorInvalidCharactersThrowsException() {
        // Arrange
        String invalidWord = "мама123";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> new Word(invalidWord),
            "Должно бросить исключение для слова с некорректными символами");
    }

    @Test
    void testGuessCorrectLetter() {
        // Arrange
        Word word = new Word("мама");

        // Act
        boolean result = word.guess('а');

        // Assert
        assertTrue(result, "Буква 'а' должна быть угадана");
        assertEquals("*а*а", word.getMask(), "Маска должна обновиться для всех 'а'");
        assertFalse(word.isFullyGuessed(), "Слово не полностью угадано");
    }

    @Test
    void testGuessIncorrectLetter() {
        // Arrange
        Word word = new Word("мама");

        // Act
        boolean result = word.guess('б');

        // Assert
        assertFalse(result, "Буква 'б' не должна быть угадана");
        assertEquals("****", word.getMask(), "Маска не должна измениться");
        assertFalse(word.isFullyGuessed(), "Слово не угадано");
    }

    @Test
    void testGuessCaseInsensitive() {
        // Arrange
        Word word = new Word("мама");

        // Act
        boolean resultUpper = word.guess('А');
        boolean resultLower = word.guess('м');

        // Assert
        assertTrue(resultUpper, "Верхний регистр 'А' должен работать");
        assertTrue(resultLower, "Нижний регистр 'а' должен работать");
        assertEquals("мама", word.getMask(), "Маска должна обновиться для всех 'а' и 'м'");
    }

    @Test
    void testGuessRepeatedLetter() {
        // Arrange
        Word word = new Word("мама");

        // Act
        boolean firstGuess = word.guess('а');
        boolean secondGuess = word.guess('а');

        // Assert
        assertTrue(firstGuess, "Первое угадывание 'а' должно быть успешным");
        assertFalse(secondGuess, "Повторное угадывание 'а' не должно менять маску");
        assertEquals("*а*а", word.getMask(), "Маска должна обновиться только один раз");
    }

    @Test
    void testIsFullyGuessedTrue() {
        // Arrange
        Word word = new Word("мама");

        // Act
        word.guess('м');
        word.guess('а');

        // Assert
        assertEquals("мама", word.getMask(), "Маска должна быть полной");
        assertTrue(word.isFullyGuessed(), "Слово должно быть полностью угадано");
    }

    @Test
    void testIsFullyGuessedFalse() {
        // Arrange
        Word word = new Word("мама");

        // Act
        word.guess('м');

        // Assert
        assertEquals("м*м*", word.getMask(), "Маска должна быть частичной");
        assertFalse(word.isFullyGuessed(), "Слово не должно быть полностью угадано");
    }
}
