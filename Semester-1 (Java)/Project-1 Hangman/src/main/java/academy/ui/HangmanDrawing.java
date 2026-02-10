package academy.ui;

public class HangmanDrawing {
    private static final String[] BASE_STAGES = {
        "\n\n",  // 0 ошибок
        " O\n\n",  // 1:
        " O|\n\n",
        " O|\n |\n",  // 2: тело
        " O|\n/|\n",  // 3: левая рука
        " O|\n/|\\\n",  // 4: правая рука
        " O|\n/|\\\n/ ",  // 5: левая нога
        " O|\n/|\\\n/ \\",  // 6: правая нога
        " O|\n/|\\\n/ \\ (погиб)",  // 7: финал
    };

    public String getStage(int mistakes, int maxMistakes) {
        if (mistakes < 0 || mistakes > maxMistakes) {
            throw new IllegalArgumentException("Ошибки должны быть от 0 до " + maxMistakes);
        }
        // Масштабируем этапы под maxErrors
        int stageIndex = (mistakes * (BASE_STAGES.length - 1)) / maxMistakes;
        return BASE_STAGES[stageIndex];
    }
}
