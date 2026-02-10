package academy.maze.dto;

/** Тип ячейки в лабиринте. WALL - стена, PATH - свободная ячейка. */
public enum CellType {
    WALL('#', "⬛"),
    PASSAGE(' ', "⬜"),
    PATH('.', "\uD83D\uDD3B"),
    START('O', "⭐"),
    END('X', "\uD83C\uDF1F");

    private final char plain;
    private final String unicode;

    CellType(char plain, String symbol) {
        this.plain = plain;
        this.unicode = symbol;
    }

    public String getSymbol(boolean useUnicode) {
        return useUnicode ? unicode : String.valueOf(plain);
    }

    public static CellType fromChar(char symbol) {
        for (CellType type : values()) {
            if (type.plain == symbol) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неизвестный символ: " + symbol);
    }

    @Override
    public String toString() {
        return getSymbol(false);
    } // по умолчанию — plain
}
