package academy.model;

import academy.model.enums.Difficulty;
import academy.model.enums.GameResult;

public class HangmanState {
    private final Word word;
    private final int maxMistakes;
    private int mistakesCount;
    private GameResult gameResult;

    public HangmanState(Word word, Difficulty difficulty) {
        this.word = word;
        mistakesCount = 0;
        this.maxMistakes = difficulty.getMaxMistakes();
        gameResult = GameResult.IN_PROGRESS;
    }

    private void updateResult() {
        if (mistakesCount >=  maxMistakes) {
            gameResult = GameResult.LOSE;
        }
        else if (word.isFullyGuessed()) {
            gameResult = GameResult.WIN;
        }
    }

    public boolean guessLetter(char letter) {
        boolean guess = word.guess(letter);
        if (!guess) {
            mistakesCount++;
        }
        updateResult();
        return guess;
    }

    public String getMask() {
        return word.getMask();
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public int getMaxMistakes() {
        return maxMistakes;
    }

    public int getMistakesCount(){
        return mistakesCount;
    }
}
