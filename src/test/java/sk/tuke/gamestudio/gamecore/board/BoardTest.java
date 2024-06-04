package sk.tuke.gamestudio.gamecore.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gamecore.models.board.Board;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CellState;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.initBoard();
    }

    @Test
    void testCheckersAreOutForColorWhenBoardIsEmpty() {
        assertFalse(board.checkersAreOut(CheckerColor.WHITE));
        assertFalse(board.checkersAreOut(CheckerColor.BLACK));
    }

    @Test
    void testCheckersAreOutForColorWhenOnlyWhiteCheckersPresent() {
        for (Cell cell : board.getBoard()) {
            if (cell.getCellState() == CellState.HAS_CHECKER && cell.getChecker().getColor() == CheckerColor.BLACK) {
                cell.setCellState(CellState.EMPTY);
                cell.setChecker(null);
            }
        }
        assertTrue(board.checkersAreOut(CheckerColor.BLACK));
        assertFalse(board.checkersAreOut(CheckerColor.WHITE));
    }

    @Test
    void testCheckersAreOutForColorWhenOnlyBlackCheckersPresent() {
        for (Cell cell : board.getBoard()) {
            if (cell.getCellState() == CellState.HAS_CHECKER && cell.getChecker().getColor() == CheckerColor.WHITE) {
                cell.setCellState(CellState.EMPTY);
                cell.setChecker(null);
            }
        }
        assertFalse(board.checkersAreOut(CheckerColor.BLACK));
        assertTrue(board.checkersAreOut(CheckerColor.WHITE));
    }

    @Test
    void testGetCell() {
        Cell cell = board.getCell(0);
        assertNotNull(cell);
    }

    @Test
    void testCheckersAreOutForColorWhenAllCheckersPresent() {
        assertFalse(board.checkersAreOut(CheckerColor.WHITE));
        assertFalse(board.checkersAreOut(CheckerColor.BLACK));
    }

    @Test
    void testCheckersAreOutForColorWhenNoCheckersPresent() {
        for (Cell cell : board.getBoard()) {
            cell.setCellState(CellState.EMPTY);
            cell.setChecker(null);
        }
        assertTrue(board.checkersAreOut(CheckerColor.WHITE));
        assertTrue(board.checkersAreOut(CheckerColor.BLACK));
    }

    @Test
    void testGetCellOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(64));
    }

    @Test
    void testInitBoard() {
        Board newBoard = new Board();
        newBoard.initBoard();
        for (Cell cell : newBoard.getBoard()) {
            assertNotNull(cell);
        }
    }

    @Test
    void testGetCellAfterInit() {
        Board newBoard = new Board();
        newBoard.initBoard();
        Cell cell = newBoard.getCell(0);
        assertNotNull(cell);
    }
}