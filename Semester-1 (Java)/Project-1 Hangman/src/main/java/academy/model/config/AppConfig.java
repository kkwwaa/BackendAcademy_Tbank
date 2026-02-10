package academy.model.config;

import java.util.*;

public class AppConfig {
    private Map<String, List<WordEntry>> wordsByCategory = new HashMap<>();
    private Map<String, DifficultyConfig> difficulties = new HashMap<>();

    public Map<String, List<WordEntry>> getWordsByCategory() {
        return wordsByCategory;
    }

    public void setWordsByCategory(Map<String, List<WordEntry>> wordsByCategory) {
        this.wordsByCategory = wordsByCategory;
    }

    public Map<String, DifficultyConfig> getDifficulties() {
        return difficulties;
    }
}
