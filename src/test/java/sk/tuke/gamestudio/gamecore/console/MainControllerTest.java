package sk.tuke.gamestudio.gamecore.console;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainControllerTest {
    private MainController mainController;

    @BeforeEach
    void setUp() {
        mainController = new MainController();
    }

    @Test
    void testConsoleCreation() {
        assertNotNull(mainController);
    }

    @Test
    void testRestartFlagAfterRestartMethod() {
        MainController.restart();
        assertTrue(MainController.isGetRestartState());
    }
}
