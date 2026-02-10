package academy.maze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка лабиринта.
 *
 * <p>Представляет отдельную позицию в лабиринте с координатами и типом содержимого.
 */
@Setter
@Getter
@AllArgsConstructor
public class Cell {
    private CellType type;
}
