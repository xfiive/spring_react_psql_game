package sk.tuke.gamestudio.gamecore.cell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Checker;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CellState;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerType;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(0, 0);
    }

    @Test
    void testConstructor() {
        assertEquals(0, cell.getRow());
        assertEquals(0, cell.getColumn());
        assertEquals(CellState.EMPTY, cell.getCellState());
        assertNull(cell.getChecker());
    }

    @Test
    void testSetCellState() {
        cell.setCellState(CellState.HAS_CHECKER);
        assertEquals(CellState.HAS_CHECKER, cell.getCellState());
    }

    @Test
    void testEquals() {
        Cell sameCell = new Cell(0, 0);
        Cell differentCell = new Cell(1, 1);
        assertTrue(cell.equals(sameCell));
        assertFalse(cell.equals(differentCell));
    }

    @Test
    void testSetChecker() {
        Checker checker = new Checker(CheckerColor.WHITE, CheckerType.MAN);
        cell.setChecker(checker);
        assertEquals(checker, cell.getChecker());
    }

    @Test
    void testToStringEmptyCell() {
        assertEquals("\033[37m.\033[0m", cell.toString());
    }

    @Test
    void testToStringCellWithChecker() {
        Checker checker = new Checker(CheckerColor.WHITE, CheckerType.MAN);
        cell.setChecker(checker);
        assertNotEquals(checker.toString(), cell.toString());
    }
}
