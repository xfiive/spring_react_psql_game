package sk.tuke.gamestudio.gamecore.console;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.annotations.observer.ObserverPattern;
import sk.tuke.gamestudio.database.security.PackageScanner;
import sk.tuke.gamestudio.gamecore.controllers.UIController;
import sk.tuke.gamestudio.gamecore.controllers.UsersController;
import sk.tuke.gamestudio.gamecore.core.Core;
import sk.tuke.gamestudio.gamecore.enums.GameResult;
import sk.tuke.gamestudio.gamecore.enums.GameState;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.gamecore.handlers.InputHandler;
import sk.tuke.gamestudio.gamecore.parsers.InputParser;

import static java.lang.System.exit;

@ObserverPattern
@Service
public class MainController {

    private static final String GAME_PACKAGE = "sk.tuke.gamestudio";

    @Getter
    private static boolean getRestartState;

    private UIController ui;

    private Turn turn;

    private Core core;

    private InputHandler inputHandler;

    private UsersController usersController;

    public static void restart() {
        getRestartState = true;
    }

    @Autowired
    public void setUi(UIController ui) {
        this.ui = ui;
    }

    @Autowired
    public void setCore(Core core) {
        this.core = core;
    }

    @Autowired
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Autowired
    public void setUsersController(UsersController usersController) {
        this.usersController = usersController;
    }

    public void play() throws Exception {
        this.startGame();
        this.gameLoop();
    }

    private void gameLoop() throws Exception {
        while (this.core.getState() != GameState.FINISHED) {
            if (this.core.getState().equals(GameState.RUNNING)) {
                ui.showGameTurn(this.core.getTurn());
                ui.showBoard(this.core.getBoard());
            }
            String[] userInput = InputParser.getUserInput();
            this.core.setTurn(this.inputHandler.handleInput(userInput, this.core.getTurn()));
            if (core.isGameFinished(this.core.getTurn(), this.core.getBoard())) {
                this.core.updateStats();
                if (!this.core.getGameResult().equals(GameResult.UNFINISHED))
                    this.ui.showGameResults(this.core.getGameResult());
                if (!this.requestUserForRestart())
                    this.usersController.getFinalUsersReviewAndFinishGame(core.getGameResult(), core.getPlayers());
                else
                    getRestartState = true;
            }
            this.restartCheck();
        }
    }

    private void startGame() throws Exception {
        this.securityCheck();
        this.startGameCore();
        getRestartState = false;
        this.usersController.enterUserAccounts(this.core.getPlayers());
        this.core.getUi().showGameRules();
    }

    private void startGameCore() {
        this.core.init();
    }

    private void securityCheck() {
        PackageScanner.scanAndCheckForUniqueClass(GAME_PACKAGE);
    }

    private void restartCheck() throws Exception {
        if (getRestartState) {
            getRestartState = false;
            this.startGame();
            this.gameLoop();
            exit(0);
        }
    }

    private boolean requestUserForRestart() {
        this.ui.showGameRestartRequest();
        return this.inputHandler.handleGameRestartRequest(InputParser.getRestartRequest());
    }

}
