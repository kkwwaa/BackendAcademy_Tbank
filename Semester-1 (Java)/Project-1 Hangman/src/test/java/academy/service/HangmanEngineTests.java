package academy.service;

import academy.model.*;
import academy.model.config.DifficultyConfig;
import academy.model.enums.Difficulty;
import academy.ui.ConsoleUI;
import academy.ui.HangmanDrawing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HangmanEngineTest {
    @Mock
    private Dictionary dictionary;
    @Mock
    private ConsoleUI ui;
    @Mock
    private HangmanDrawing hangmanDrawing;

    private HangmanEngine engine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        engine = new HangmanEngine(dictionary, ui, hangmanDrawing);

        // Настраиваем Difficulty.MEDIUM для playNonInteractive
        DifficultyConfig config = new DifficultyConfig();
        config.setMaxMistakes(6);
        config.setMinWordLength(4);
        config.setMaxWordLength(7);
        Difficulty.MEDIUM.configure(config);
    }

    @Test
    void testPlayInteractiveWin() {
        // Arrange
        when(ui.chooseCategory(anyList())).thenReturn("семья");
        when(ui.chooseDifficulty()).thenReturn(Difficulty.EASY);
        when(dictionary.chooseWord("семья", Difficulty.EASY)).thenReturn("мама");
        when(ui.getLetter(anyString())).thenReturn('м', 'а');
        when(dictionary.getHint("мама")).thenReturn("Близкий родственник");

        // Настраиваем Difficulty.EASY
        DifficultyConfig easyConfig = new DifficultyConfig();
        easyConfig.setMaxMistakes(8);
        easyConfig.setMinWordLength(4);
        easyConfig.setMaxWordLength(6);
        Difficulty.EASY.configure(easyConfig);

        // Act
        engine.playInteractive();

        // Assert
        verify(ui).chooseCategory(anyList());
        verify(ui).chooseDifficulty();
        verify(dictionary).chooseWord("семья", Difficulty.EASY);
        verify(ui, times(2)).printState(any(HangmanState.class), eq(hangmanDrawing), anyBoolean());
        verify(ui, times(2)).getLetter("Близкий родственник");
        verify(ui).printResult(any(HangmanState.class));
    }

    @Test
    void testPlayInteractiveLose() {
        // Arrange
        when(ui.chooseCategory(anyList())).thenReturn("семья");
        when(ui.chooseDifficulty()).thenReturn(Difficulty.EASY);
        when(dictionary.chooseWord("семья", Difficulty.EASY)).thenReturn("мама");
        when(ui.getLetter(anyString())).thenReturn('б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и');
        when(dictionary.getHint("мама")).thenReturn("Близкий родственник");

        // Настраиваем Difficulty.EASY
        DifficultyConfig easyConfig = new DifficultyConfig();
        easyConfig.setMaxMistakes(8);
        easyConfig.setMinWordLength(4);
        easyConfig.setMaxWordLength(6);
        Difficulty.EASY.configure(easyConfig);

        // Act
        engine.playInteractive();

        // Assert
        verify(ui).chooseCategory(anyList());
        verify(ui).chooseDifficulty();
        verify(dictionary).chooseWord("семья", Difficulty.EASY);
        verify(ui, times(8)).printState(any(HangmanState.class), eq(hangmanDrawing), anyBoolean());
        verify(ui, times(8)).getLetter("Близкий родственник");
        verify(ui).printResult(any(HangmanState.class));
    }

    @Test
    void testPlayNonInteractiveVoloknoTolokno() {
        // Arrange
        String secret = "волокно";
        String guess = "толокно";

        // Act
        String result = engine.playNonInteractive(secret, guess);

        // Assert
        assertEquals("*олокно;NEG", result, "Должно вернуть *олокно;NEG для волокно и толокно");
    }

    @Test
    void testPlayNonInteractiveVoloknoBarakhlo() {
        // Arrange
        String secret = "волокно";
        String guess = "барахло";

        // Act
        String result = engine.playNonInteractive(secret, guess);

        // Assert
        assertEquals("*оло**о;NEG", result, "Должно вернуть *оло**о;NEG для волокно и барахло");
    }

    @Test
    void testPlayNonInteractiveOknoOkno() {
        // Arrange
        String secret = "окно";
        String guess = "окно";

        // Act
        String result = engine.playNonInteractive(secret, guess);

        // Assert
        assertEquals("окно;POS", result, "Должно вернуть окно;POS для окно и окно");
    }
}
