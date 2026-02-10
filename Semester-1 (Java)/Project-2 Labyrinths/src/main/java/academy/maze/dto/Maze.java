package academy.maze.dto;

import academy.maze.ui.enums.DisplayMode;
import lombok.Getter;

/** Лабиринт. */
@Getter
public class Maze {
    private final int height;
    private final int width;
    private final Cell[][] cells;

    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.cells = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new Cell(CellType.WALL);
            }
        }
    }

    public void setCellType(Point point, CellType cellType) {
        cells[point.y()][point.x()].setType(cellType);
    }

    public CellType getCellType(Point point) {
        return cells[point.y()][point.x()].getType();
    }

    public Maze getCopy() {
        Maze copy = new Maze(height, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell original = cells[y][x];
                CellType type = original.getType();
                copy.cells[y][x] = new Cell(type);
            }
        }
        return copy;
    }

    public Maze mazeWithPath(Path path) {
        Maze mazeWithPath = this.getCopy();
        path.markOnMaze(mazeWithPath);
        return mazeWithPath;
    }

    @Override
    public String toString() {
        return toString(DisplayMode.PLAIN);
    }

    public String toString(DisplayMode mode) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean useUnicode = mode == DisplayMode.UNICODE;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CellType cell = getCellType(new Point(y, x));
                stringBuilder.append(cell.getSymbol(useUnicode));
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    public String toStringForCLI() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean useUnicode = false;

        stringBuilder.append(CellType.WALL.getSymbol(useUnicode).repeat(Math.max(0, width + 2)));
        stringBuilder.append('\n');
        for (int y = 0; y < height; y++) {
            stringBuilder.append(CellType.WALL.getSymbol(useUnicode));

            for (int x = 0; x < width; x++) {
                CellType cell = getCellType(new Point(y, x));
                stringBuilder.append(cell.getSymbol(useUnicode));
            }

            stringBuilder.append(CellType.WALL.getSymbol(useUnicode));
            stringBuilder.append('\n');
        }
        stringBuilder.append(CellType.WALL.getSymbol(useUnicode).repeat(Math.max(0, width + 2)));
        stringBuilder.append('\n');

        return stringBuilder.toString();
    }
}
