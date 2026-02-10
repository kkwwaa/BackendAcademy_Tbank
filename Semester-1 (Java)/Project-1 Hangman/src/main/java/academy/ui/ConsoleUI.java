package academy.ui;

import academy.model.*;
import academy.model.enums.Difficulty;
import academy.model.enums.GameResult;

import java.util.*;
import java.util.function.Function;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private boolean hintUsed = false;

    // Добавляем метод для переопределения в тестах
    protected Scanner getScanner() {
        return scanner;
    }

    private <T> T chooseFromList(List<T> options, String prompt, Function<T, String> formatter) {
        System.out.println(prompt);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + formatter.apply(options.get(i)));
        }

        while (true) {
            System.out.print("Введите номер (или нажмите Enter для случайного выбора): ");
            String input = getScanner().nextLine().trim();

            if (input.isEmpty()) {
                T randomChoice = options.get(new Random().nextInt(options.size()));
                System.out.println("Выбрано случайно: " + formatter.apply(randomChoice));
                return randomChoice;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= options.size()) {
                    return options.get(choice - 1);
                }
                System.out.println("Ошибка: выберите номер от 1 до " + options.size());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число");
            }
        }
    }

    public String chooseCategory(List<String> categories) {
        return chooseFromList(categories, "Выберите категорию:", s -> s);
    }

    public Difficulty chooseDifficulty() {
        return chooseFromList(List.of(Difficulty.values()),
            "Выберите сложность:",
            difficulty -> difficulty + " (" + difficulty.getMaxMistakes() + " ошибок, длина слова от " + difficulty.getMinWordLength() + " до " + difficulty.getMaxWordLength() + " символов)"
        );
    }

    public void printMask(HangmanState hangmanState) {
        System.out.println("Слово: " + hangmanState.getMask());
    }

    public void printState(HangmanState hangmanState, HangmanDrawing drawing, boolean guess) {
        printMask(hangmanState);
        System.out.println("Виселица:\n" + drawing.getStage(hangmanState.getMistakesCount(), hangmanState.getMaxMistakes()));

        if (!guess)
            System.out.println("Осталось попыток: " + (hangmanState.getMaxMistakes() - hangmanState.getMistakesCount()));
    }

    public void printResult(HangmanState hangmanState) {
        printMask(hangmanState);
        if (hangmanState.getGameResult() == GameResult.WIN) {
            System.out.println("Вы выиграли!");
        } else {
            System.out.println("Вы проиграли(");
        }
    }

    public char getLetter(String hint) {
        while (true) {
            System.out.print("Введите букву (или 'h' для подсказки): ");
            String input = getScanner().nextLine().toLowerCase();
            if (input.equals("h")) {
                if (hintUsed) {
                    System.out.println("Подсказка уже использована!");
                    continue;
                }
                hintUsed = true;
                System.out.println("Подсказка: " + hint);
                continue;
            }
            if (input.length() != 1 || !input.matches("[а-яА-Я]")) {
                System.out.println("Ошибка: введите одну русскую букву!");
                continue;
            }
            return input.charAt(0);
        }
    }
}
