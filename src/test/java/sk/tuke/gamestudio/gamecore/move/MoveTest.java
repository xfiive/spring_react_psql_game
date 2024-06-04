package sk.tuke.gamestudio.gamecore.move;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Checker;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CellState;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.moves.Move;
import sk.tuke.gamestudio.gamecore.models.moves.enums.MoveState;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    private ArrayList<Cell> cells;

    @BeforeEach
    void setUp() {
        cells = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            Cell cell = new Cell(i / 8, i % 8);
            Checker checker = new Checker();
            cell.setChecker(checker);
            cells.add(cell);
        }
    }

    @Test
    void testHasValidMovesWithEmptyCell() {
        assertFalse(Move.hasValidMoves(cells, cells.get(0)));
    }

    @Test
    void testHasValidMovesWithKingWhiteChecker() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(0).getChecker().makeKing();
        assertTrue(Move.hasValidMoves(cells, cells.get(0)));
    }

    @Test
    void testHasValidMovesWithKingBlackChecker() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.BLACK);
        cells.get(0).getChecker().makeKing();
        assertTrue(Move.hasValidMoves(cells, cells.get(0)));
    }

    @Test
    void testExecuteMoveWithEmptyStartCell() {
        Move move = new Move(cells.get(0), cells.get(1), Turn.WHITE);
        assertEquals(MoveState.FAILED_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithSameStartAndEndCell() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        Move move = new Move(cells.get(0), cells.get(0), Turn.WHITE);
        assertEquals(MoveState.FAILED_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithEndCellHavingChecker() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(1).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(1).setCheckerColor(CheckerColor.BLACK);
        Move move = new Move(cells.get(0), cells.get(1), Turn.WHITE);
        assertEquals(MoveState.FAILED_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithOutOfBoundsEndCell() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        Move move = new Move(cells.get(0), new Cell(-1, -1), Turn.WHITE);
        assertEquals(MoveState.FAILED_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithValidMove() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(1).setCellState(CellState.EMPTY);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        Move move = new Move(cells.get(0), cells.get(1), Turn.WHITE);
        assertEquals(MoveState.SUCCESSFUL_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithInvalidMove() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(1).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(1).setCheckerColor(CheckerColor.BLACK);
        Move move = new Move(cells.get(0), cells.get(1), Turn.WHITE);
        assertEquals(MoveState.FAILED_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithJumpOverChecker() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(9).setCellState(CellState.HAS_CHECKER);
        cells.get(9).setCheckerColor(CheckerColor.BLACK);
        Move move = new Move(cells.get(0), cells.get(18), Turn.WHITE);
        assertEquals(MoveState.SUCCESSFUL_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithJumpOverCheckerAndEmptyEndCell() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(9).setCellState(CellState.HAS_CHECKER);
        cells.get(9).setCheckerColor(CheckerColor.BLACK);
        Move move = new Move(cells.get(0), cells.get(18), Turn.WHITE);
        assertEquals(MoveState.SUCCESSFUL_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithInvalidJump() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(9).setCellState(CellState.HAS_CHECKER);
        cells.get(9).setCheckerColor(CheckerColor.BLACK);
        cells.get(18).setCellState(CellState.HAS_CHECKER);
        cells.get(18).setCheckerColor(CheckerColor.WHITE);
        Move move = new Move(cells.get(0), cells.get(27), Turn.WHITE);
        assertEquals(MoveState.FAILED_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithSuccessfulJump() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(9).setCellState(CellState.HAS_CHECKER);
        cells.get(9).setCheckerColor(CheckerColor.BLACK);
        cells.get(18).setCellState(CellState.HAS_CHECKER);
        cells.get(18).setCheckerColor(CheckerColor.BLACK);
        Move move = new Move(cells.get(0), cells.get(27), Turn.WHITE);
        assertNotEquals(MoveState.SUCCESSFUL_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithCheckerToTake() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(9).setCellState(CellState.HAS_CHECKER);
        cells.get(9).setCheckerColor(CheckerColor.BLACK);
        Move move = new Move(cells.get(0), cells.get(18), Turn.WHITE);
        assertNotEquals(MoveState.NOT_FINISHED_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithNoCheckerToTake() {
        cells.get(0).setCellState(CellState.HAS_CHECKER);
        cells.get(0).setCheckerColor(CheckerColor.WHITE);
        cells.get(1).setCellState(CellState.HAS_CHECKER);
        cells.get(1).setCheckerColor(CheckerColor.BLACK);
        Move move = new Move(cells.get(0), cells.get(1), Turn.WHITE);
        assertNotEquals(MoveState.SUCCESSFUL_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithSuccessfulKingMove() {
        cells.get(7).setCellState(CellState.HAS_CHECKER);
        cells.get(7).setCheckerColor(CheckerColor.WHITE);
        Move move = new Move(cells.get(7), cells.get(1), Turn.WHITE);
        assertEquals(MoveState.SUCCESSFUL_WHITE, move.execute(cells));
    }

    @Test
    void testExecuteMoveWithInvalidKingMove() {
        cells.get(7).setCellState(CellState.HAS_CHECKER);
        cells.get(7).setCheckerColor(CheckerColor.WHITE);
        Move move = new Move(cells.get(7), cells.get(2), Turn.WHITE);
        assertNotEquals(MoveState.FAILED_WHITE, move.execute(cells));
    }
}
