package sk.tuke.gamestudio.gamecore.checker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Checker;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerType;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {
    private Checker whiteManChecker;
    private Checker blackKingChecker;

    @BeforeEach
    void setUp() {
        whiteManChecker = new Checker(CheckerColor.WHITE);
        blackKingChecker = new Checker(CheckerColor.BLACK, CheckerType.KING);
    }

    @Test
    void testConstructorAndGetter() {
        assertEquals(CheckerColor.WHITE, whiteManChecker.getColor());
        assertEquals(CheckerType.MAN, whiteManChecker.getType());
        assertEquals(CheckerColor.BLACK, blackKingChecker.getColor());
        assertEquals(CheckerType.KING, blackKingChecker.getType());
    }

    @Test
    void testSetType() {
        whiteManChecker.setType(CheckerType.KING);
        assertEquals(CheckerType.KING, whiteManChecker.getType());
    }

    @Test
    void testMakeKing() {
        whiteManChecker.makeKing();
        assertEquals(CheckerType.KING, whiteManChecker.getType());
    }

    @Test
    void testToString() {
        assertEquals("\033[34mW\033[0m", whiteManChecker.toString());
        assertEquals("\033[31mB\033[0m", blackKingChecker.toString());
    }
}
