package sk.tuke.gamestudio.gamecore.models.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.gamecore.models.checkers.CellFactory;
import sk.tuke.gamestudio.gamecore.models.checkers.CheckerFactory;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Checker;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CellState;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerType;
import sk.tuke.gamestudio.annotations.factory.FactoryPattern;
import sk.tuke.gamestudio.annotations.factory.FactoryProduct;

import java.util.ArrayList;

@Getter
@Setter
@FactoryPattern("Board Factory")
@Service
public class Board {

    public static final int BOARD_SIZE = 64;

    private ArrayList<Cell> board;

    public boolean checkersAreOut(CheckerColor color) {
        for (Cell cell : board) {
            if (cell.getCellState().equals(CellState.HAS_CHECKER)) {
                if (cell.getChecker().getColor().equals(color)) {
                    return false;
                }
            }
        }
        return true;
    }

    @FactoryProduct("Cell")
    public void initBoard() {
        this.board = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE / 8; row++) {
            for (int column = 0; column < BOARD_SIZE / 8; column++) {
                Cell cell = CellFactory.createCell(row, column);
                cell.setCellState(CellState.EMPTY);
                this.board.add(row * 8 + column, cell);
            }
        }

        for (int row = 0; row < BOARD_SIZE / 8; row++) {
            for (int column = 0; column < BOARD_SIZE / 8; column++) {
                int index = row * 8 + column;
                if (index < 24)
                    index += 40;
                else if (40 <= index)
                    index -= 40;
                switch (row) {
                    case 0:
                    case 2:
                        if (column % 2 == 0) {
                            Checker whiteChecker = CheckerFactory.createChecker(CheckerColor.WHITE, CheckerType.MAN);
                            this.board.get(index).setChecker(whiteChecker);
                            this.board.get(index).setCellState(CellState.HAS_CHECKER);
                        }
                        break;
                    case 1:
                        if (column % 2 == 1) {
                            Checker whiteChecker = CheckerFactory.createChecker(CheckerColor.WHITE, CheckerType.MAN);
                            this.board.get(index).setChecker(whiteChecker);
                            this.board.get(index).setCellState(CellState.HAS_CHECKER);
                        }
                        break;
                    case 5:
                    case 7:
                        if (column % 2 == 1) {
                            Checker blackChecker = CheckerFactory.createChecker(CheckerColor.BLACK, CheckerType.MAN);
                            this.board.get(index).setChecker(blackChecker);
                            this.board.get(index).setCellState(CellState.HAS_CHECKER);
                        }
                        break;
                    case 6:
                        if (column % 2 == 0) {
                            Checker blackChecker = CheckerFactory.createChecker(CheckerColor.BLACK, CheckerType.MAN);
                            this.board.get(index).setChecker(blackChecker);
                            this.board.get(index).setCellState(CellState.HAS_CHECKER);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public Cell getCell(int index) {
        return board.get(index);
    }

}
