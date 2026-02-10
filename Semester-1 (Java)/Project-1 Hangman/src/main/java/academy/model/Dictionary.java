package academy.model;

import academy.model.config.AppConfig;
import academy.model.config.WordEntry;
import academy.model.enums.Difficulty;

import java.util.*;

public class Dictionary {
    private final Map<String, List<WordEntry>> wordsByCategory;
    private final Random random = new Random();

    public Dictionary(AppConfig config) {
        this.wordsByCategory = config.getWordsByCategory();
    }

    public List<String> getCategories() {
        return List.copyOf(wordsByCategory.keySet());
    }

    public String chooseWord(String category, Difficulty difficulty) {
        List<WordEntry> entries = wordsByCategory.getOrDefault(category, List.of());
        List<String> filtered = entries.stream()
            .filter(entry -> entry.getWord().length() >= difficulty.getMinWordLength()
            && entry.getWord().length() <= difficulty.getMaxWordLength())
            .map(WordEntry::getWord)
            .toList();

        if (filtered.isEmpty()) {
            throw new IllegalStateException("Нет слов в категории '" + category + "' с длиной " + difficulty.getMinWordLength() + "-" + difficulty.getMaxWordLength());
        }
        return filtered.get(random.nextInt(filtered.size()));
    }

    public String getHint(String word) {
        return wordsByCategory.values().stream()
            .flatMap(List::stream)
            .filter(entry -> entry.getWord().equals(word))
            .findFirst()
            .map(WordEntry::getHint)
            .orElse("Нет подсказки");
    }
}
