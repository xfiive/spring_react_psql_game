package sk.tuke.gamestudio.gamecore.models.checkers.components;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.annotations.factory.FactoryProduct;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CellState;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;

@Getter
@FactoryProduct("Cell")
public class Cell {

    private static final String emptyCellSkin = "\033[37m" + "." + "\033[0m";
    private final int row;
    private final int column;
    private CellState cellState;
    private Checker checker;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.cellState = CellState.EMPTY;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public boolean equals(@NotNull Cell cell) {
        return this.row == cell.getRow() && this.column == cell.getColumn();
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    @Override
    public String toString() {
        if (this.cellState == CellState.HAS_CHECKER)
            return this.checker.toString();
        else
            return emptyCellSkin;
    }

    public void setCheckerColor(CheckerColor checkerColor) {
        this.checker.setColor(checkerColor);
    }
}
