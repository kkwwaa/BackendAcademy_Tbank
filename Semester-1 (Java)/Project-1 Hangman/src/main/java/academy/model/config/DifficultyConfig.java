package academy.model.config;

public class DifficultyConfig {
    private int maxMistakes;
    private int minWordLength;
    private int maxWordLength;

    public DifficultyConfig() {

    }

    public DifficultyConfig(int maxMistakes, int minWordLength, int maxWordLength) {
        this.maxMistakes = maxMistakes;
        this.minWordLength = minWordLength;
        this.maxWordLength = maxWordLength;
    }

    public int getMaxMistakes() {
        return maxMistakes;
    }

    public void setMaxMistakes(int maxMistakes) {
        this.maxMistakes = maxMistakes;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public void setMinWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public void setMaxWordLength(int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }
}
