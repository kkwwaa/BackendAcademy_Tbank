package academy.maze.ui.input;

import java.util.Scanner;

public class ConsoleInput implements InputProvider {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public String readLine() {
        return sc.nextLine();
    }

    @Override
    public int readInt(int max) {
        while (true) {
            String line = readLine();
            if (line.matches("\\d+")) {
                int i = Integer.parseInt(line) - 1;
                if (i >= 0 && i < max) {
                    return i;
                } else {
                    System.out.println("Введенное число некорректно, пожалуйста, попробуйте еще раз");
                }
            } else {
                System.out.println("Введенное значение некорректно, пожалуйста, попробуйте еще раз");
            }
        }
    }
}
