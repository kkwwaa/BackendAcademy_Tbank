package academy.service;

import academy.model.enums.Difficulty;
import academy.model.enums.GameResult;
import academy.ui.ConsoleUI;
import academy.model.*;
import academy.ui.HangmanDrawing;

public class HangmanEngine {
    private final Dictionary dictionary;
    private final ConsoleUI ui;
    private final HangmanDrawing hangmanDrawing;

    public HangmanEngine(Dictionary dictionary, ConsoleUI ui,  HangmanDrawing hangmanDrawing) {
        this.dictionary = dictionary;
        this.ui = ui;
        this.hangmanDrawing = hangmanDrawing;
    }

    public void playInteractive() {
        String category = ui.chooseCategory(dictionary.getCategories());
        Difficulty difficulty = ui.chooseDifficulty();
        String secret = dictionary.chooseWord(category, difficulty);
        HangmanState state = new HangmanState(new Word(secret), difficulty);
        boolean guess = true;

        while (state.getGameResult() == GameResult.IN_PROGRESS) {
            ui.printState(state, hangmanDrawing, guess);
            char letter = ui.getLetter(dictionary.getHint(secret));
            guess = state.guessLetter(letter);
        }

        ui.printResult(state);
    }

    public String playNonInteractive(String secret, String guess) {
        int minLength = Difficulty.MEDIUM.getMinWordLength();
        int maxLength = Difficulty.MEDIUM.getMaxWordLength();
        if (secret.length() < minLength || secret.length() > maxLength) {
            throw new IllegalArgumentException(
                "Длина слова должна быть от " + minLength + " до " + maxLength + " символов");
        }

        HangmanState state = new HangmanState(new Word(secret), Difficulty.MEDIUM);
        for (char letter : guess.toCharArray()) {
            state.guessLetter(letter);
            if (state.getGameResult() != GameResult.IN_PROGRESS) {
                break;
            }
        }
        String result = state.getGameResult() == GameResult.WIN ? "POS" : "NEG";
        return state.getMask() + ";" + result;
    }
}
