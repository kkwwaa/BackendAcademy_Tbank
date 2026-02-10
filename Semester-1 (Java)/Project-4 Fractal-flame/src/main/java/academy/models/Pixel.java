package academy.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Pixel {
    private int red;
    private int green;
    private int blue;
    private int hitCount;

    /** Увеличение счётчика попаданий */
    public void incrementHitCount() {
        hitCount++;
    }

    /**
     * Смешивание цвета с учётом накопленных попаданий. Простая логика усреднения: новые значения добавляются и
     * усредняются.
     */
    public void mixColor(int redNew, int greenNew, int blueNew) {
        if (hitCount == 0) {
            this.red = redNew;
            this.green = greenNew;
            this.blue = blueNew;
        } else {
            this.red = (this.red * hitCount + redNew) / (hitCount + 1);
            this.green = (this.green * hitCount + greenNew) / (hitCount + 1);
            this.blue = (this.blue * hitCount + blueNew) / (hitCount + 1);
        }
    }
}
