package sk.tuke.gamestudio.gamecore.handlers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.gamecore.console.MainController;
import sk.tuke.gamestudio.gamecore.controllers.UIController;
import sk.tuke.gamestudio.gamecore.controllers.UsersController;
import sk.tuke.gamestudio.gamecore.core.Core;
import sk.tuke.gamestudio.gamecore.enums.GameState;
import sk.tuke.gamestudio.gamecore.enums.Turn;

@Service
public class InputHandler {

    private Core core;

    private UsersController usersController;

    @Autowired
    public void setCore(Core core) {
        this.core = core;
    }

    @Autowired
    public void setUserAccountsController(UsersController usersController) {
        this.usersController = usersController;
    }

    public Turn handleInput(String @NotNull [] input, Turn turn) throws InterruptedException {
        if (input.length == 4 || input.length == 5) {
            return this.handleMoveInput(input, turn);
        } else {
            if (input.length == 1) {
                this.handleCommandInput(input);
            } else {
                System.out.println("Wrong input!");
            }
        }
        return turn;
    }

    public void handleCommandInput(String @NotNull [] input) throws InterruptedException {
        String command = input[0];
        switch (command) {
            case "pause", "p":
                if (this.core.getState() == GameState.RUNNING) {
                    this.core.setState(GameState.PAUSED);
                    this.core.getUi().showMenu();
                } else {
                    System.out.println(UIController.FAILED_TO_PAUSE);
                }
                break;
            case "continue", "c":
                if (this.core.getState() == GameState.PAUSED) {
                    this.core.setState(GameState.RUNNING);
                } else {
                    System.out.println(UIController.FAILED_TO_CONTINUE);
                }
                break;
            case "showstats", "stats", "shs":
                if (this.core.getState() == GameState.PAUSED) {
                    this.usersController.getUsersService().showStats(this.core.getPlayers());
                } else {
                    System.out.println(UIController.FAILED_TO_SHOW_STATS);
                }
                break;
            case "help", "h":
                this.core.getUi().showMenu();
                break;
            case "newgame":
                if ((this.core.getState() == GameState.PAUSED || this.core.getState() == GameState.FINISHED)) {
                    MainController.restart();
                } else {
                    System.out.println(UIController.FAILED_TO_RESTART_GAME);
                }
                break;
            case "endgame":
                this.usersController.getFinalUsersReviewAndFinishGame(this.core.getGameResult(), this.core.getPlayers());
                break;
            case "secret":
                if (this.core.getState().equals(GameState.PAUSED))
                    this.showSecret();
                break;
            default:
                break;
        }
    }

    private @NotNull @Unmodifiable Turn handleMoveInput(String[] input, Turn turn) {
        String concatenatedInput = String.join("", input);
        this.core.makeMove(concatenatedInput, turn);
        return this.core.getTurn();
    }


    public boolean handleGameRestartRequest(String @NotNull [] input) {
        String line = input[0].toLowerCase();
        return line.equals("yes") || line.equals("y");
    }

    private void showSecret() throws InterruptedException {
        System.out.println("Buy buy, my little friend :), you've found a kind of an easter-egg. But for now... The time of endless loops begins!");
        Thread.sleep(3000);
        int iter = 0;
        while (iter < Integer.MAX_VALUE) {
            System.out.println("CATS CATS CATS I LOVE CATS");
            iter++;
        }
    }

}
