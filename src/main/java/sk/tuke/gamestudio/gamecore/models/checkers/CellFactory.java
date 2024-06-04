package sk.tuke.gamestudio.gamecore.models.checkers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.annotations.factory.FactoryProduct;

@FactoryProduct("Cell")
public class CellFactory {

    @Contract(value = "_, _ -> new", pure = true)
    @FactoryProduct("Cell")
    public static @NotNull Cell createCell(int row, int column) {
        return new Cell(row, column);
    }
}
