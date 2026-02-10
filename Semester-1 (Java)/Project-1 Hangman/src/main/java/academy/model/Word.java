package academy.model;

import java.util.Arrays;

public class Word {
    private final String secret;
    private final char[] mask;
    private final boolean[] revealed;

    public Word(String secret) {
        if (!secret.matches("[а-яА-Я]+")) {
            throw new IllegalArgumentException("Слово должно содержать только русские буквы");
        }
        this.secret = secret.toLowerCase();
        this.mask = new char[secret.length()];
        this.revealed = new boolean[secret.length()];
        Arrays.fill(this.mask, '*');
    }

    public boolean guess(char letter) {
        letter = Character.toLowerCase(letter);
        boolean guess = false;
        for (int i = 0; i < this.secret.length(); i++) {
            if (this.secret.charAt(i) == letter) {
                if (revealed[i])
                    break;
                mask[i] = letter;
                revealed[i] = true;
                guess = true;
            }
        }
        return guess;
    }

    public String getMask() {
        return new String(mask);
    }

    public boolean isFullyGuessed() {
        for (boolean letter : revealed) {
            if (!letter) return false;
        }
        return true;
    }
}
