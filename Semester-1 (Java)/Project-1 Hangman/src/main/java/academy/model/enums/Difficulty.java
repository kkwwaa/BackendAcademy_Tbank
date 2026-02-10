package academy.model.enums;

import academy.model.config.DifficultyConfig;

public enum Difficulty {
    EASY, MEDIUM, HARD;

    private int maxMistakes;
    private int minWordLength;
    private int maxWordLength;

    public void configure(DifficultyConfig config) {
        this.maxMistakes = config.getMaxMistakes();
        this.minWordLength = config.getMinWordLength();
        this.maxWordLength = config.getMaxWordLength();
    }

    public int getMaxMistakes() {
        return maxMistakes;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }
}
