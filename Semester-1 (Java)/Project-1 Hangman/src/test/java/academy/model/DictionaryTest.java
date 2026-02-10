package academy.model;

import academy.model.config.*;
import academy.model.enums.Difficulty;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DictionaryTest {
    @Mock
    private AppConfig appConfig;
    @Mock
    private DifficultyConfig difficultyConfig;
    @Mock
    private Difficulty difficulty;

    private Dictionary dictionary;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dictionary = new Dictionary(appConfig);

        // Настраиваем моки для AppConfig
        WordEntry word1 = mock(WordEntry.class);
        when(word1.getWord()).thenReturn("мама");
        when(word1.getHint()).thenReturn("Близкий родственник");

        WordEntry word2 = mock(WordEntry.class);
        when(word2.getWord()).thenReturn("папа");
        when(word2.getHint()).thenReturn("Близкий родственник");

        WordEntry word3 = mock(WordEntry.class);
        when(word3.getWord()).thenReturn("яблоко");
        when(word3.getHint()).thenReturn("Фрукт");

        Map<String, List<WordEntry>> wordsByCategory = Map.of(
            "семья", Arrays.asList(word1, word2),
            "фрукты", Arrays.asList(word3)
        );
        when(appConfig.getWordsByCategory()).thenReturn(wordsByCategory);

        // Настраиваем моки для Difficulty и DifficultyConfig
        when(difficultyConfig.getMinWordLength()).thenReturn(4);
        when(difficultyConfig.getMaxWordLength()).thenReturn(6);
        when(difficultyConfig.getMaxMistakes()).thenReturn(8);
        when(difficulty.getMinWordLength()).thenReturn(4);
        when(difficulty.getMaxWordLength()).thenReturn(6);
    }

    @Test
    void testGetCategoriesReturnsAllCategories() {
        // Arrange
        Dictionary dictionary = new Dictionary(appConfig);

        // Act
        List<String> categories = dictionary.getCategories();

        // Assert
        assertEquals(2, categories.size(), "Должно быть 2 категории");
        assertTrue(categories.contains("семья"), "Категория 'семья' должна быть");
        assertTrue(categories.contains("фрукты"), "Категория 'фрукты' должна быть");
    }

    @Test
    void testChooseWordValidCategoryAndLength() {
        // Arrange
        Dictionary dictionary = new Dictionary(appConfig);

        // Act
        String chosenWord = dictionary.chooseWord("семья", difficulty);

        // Assert
        assertTrue(List.of("мама", "папа").contains(chosenWord),
            "Выбрано слово 'мама' или 'папа' (4 буквы, в диапазоне 4-6)");
    }

    @Test
    void testChooseWordEmptyCategoryThrowsException() {
        // Arrange
        Dictionary dictionary = new Dictionary(appConfig);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> dictionary.chooseWord("животные", difficulty),
            "Должно бросить исключение для пустой категории");
        assertTrue(exception.getMessage().contains("Нет слов в категории 'животные'"),
            "Сообщение об ошибке должно содержать название категории");
    }

    @Test
    void testChooseWordNoMatchingLengthThrowsException() {
        // Arrange
        Dictionary dictionary = new Dictionary(appConfig);
        Difficulty hardDifficulty = Difficulty.HARD;
        DifficultyConfig hardConfig = new DifficultyConfig();
        hardConfig.setMaxMistakes(5);
        hardConfig.setMinWordLength(7);
        hardConfig.setMaxWordLength(20);
        hardDifficulty.configure(hardConfig);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> dictionary.chooseWord("семья", hardDifficulty),
            "Должно бросить исключение, если нет слов длиной 7+");
        assertTrue(exception.getMessage().contains("7-20"),
            "Сообщение об ошибке должно содержать диапазон длин");
    }

    @Test
    void testGetHintFound() {
        // Arrange
        Dictionary dictionary = new Dictionary(appConfig);

        // Act
        String hint = dictionary.getHint("мама");

        // Assert
        assertEquals("Близкий родственник", hint, "Подсказка для 'мама' должна быть корректной");
    }

    @Test
    void testGetHintNotFound() {
        // Arrange
        Dictionary dictionary = new Dictionary(appConfig);

        // Act
        String hint = dictionary.getHint("неизвестно");

        // Assert
        assertEquals("Нет подсказки", hint, "Должно вернуть 'Нет подсказки' для неизвестного слова");
    }
}
