package sk.tuke.gamestudio.gamecore.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gamecore.enums.GameResult;
import sk.tuke.gamestudio.gamecore.enums.GameState;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.gamecore.models.board.Board;

import static org.junit.jupiter.api.Assertions.*;

public class CoreTest {

    private Core core;

    @BeforeEach
    void setUp() throws Exception {
        core = new Core();
        core.init();
    }

    @Test
    void testInitMethod() {
        assertDoesNotThrow(() -> core.init());
    }

    @Test
    void testIsGameFinishedMethod() {
        core.setGameResult(null);
        assertTrue(core.isGameFinished(Turn.WHITE, new Board()));
    }

    @Test
    void testMakeMoveMethod() {
        String[] cellsCoordinates = {"A", "1", "B", "2"};
        assertEquals(Turn.WHITE, core.makeMove(cellsCoordinates, Turn.WHITE));
    }

    @Test
    void testSetStateMethod() {
        core.setState(GameState.FINISHED);
        assertEquals(GameState.FINISHED, core.getState());
    }

    @Test
    void testSetGameResultMethod() {
        core.setGameResult(GameResult.WHITE_WIN);
        assertEquals(GameResult.WHITE_WIN, core.getGameResult());
    }
}
