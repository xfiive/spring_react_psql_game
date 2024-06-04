package sk.tuke.gamestudio.entity.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.gamecore.controllers.UIController;
import sk.tuke.gamestudio.gamecore.core.Core;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.server.dto.MoveRespond;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    private Core gameCore;

    private UIController uiController;

    private String playerOne;
    private String playerTwo;

    public GameSession(String firstPlayer, String secondPlayer) {
        this.playerOne = firstPlayer;
        this.playerTwo = secondPlayer;
        this.uiController = new UIController();
        this.gameCore = new Core();
    }

    public String surrenderForPlayer(String playerNickname) {
        return this.gameCore.surrenderForPlayer(playerNickname);
    }

    public void resetPlayers(String playerOne, String playerTwo) {
        if (this.playerOne != null) {
            if (this.playerOne.equals(playerOne)) {
                this.playerOne = null;
                this.gameCore.getPlayers().setFirst(null);
            }
            if (this.playerOne != null && this.playerOne.equals(playerTwo)) {
                this.playerOne = null;
                this.gameCore.getPlayers().setFirst(null);
            }
        }
        if (this.playerTwo != null) {
            if (this.playerTwo.equals(playerOne)) {
                this.playerTwo = null;
                this.gameCore.getPlayers().setSecond(null);
            }
            if (this.playerTwo != null && this.playerTwo.equals(playerTwo)) {
                this.playerTwo = null;
                this.gameCore.getPlayers().setSecond(null);
            }
        }
    }

    public Turn getGameTurnOnPlayer(@NotNull String currentPlayer) {
        if (currentPlayer.equals(playerOne))
            return Turn.WHITE;
        else {
            if (currentPlayer.equals(playerTwo))
                return Turn.BLACK;
            else
                return Turn.UNDEFINED;
        }
    }

    public void startGame() {
        gameCore.init();
    }

    public boolean isFull() {
        return playerOne != null && playerTwo != null;
    }

    public void addPlayer(String playerName) {
        if (playerTwo == null && !playerName.equals(playerOne)) {
            playerTwo = playerName;
        }
    }

    public boolean hasPlayer(@NotNull String playerName) {
        System.out.println("Player Name: " + playerName);
        System.out.println("Player1    : " + playerOne);
        System.out.println("Player2    : " + playerTwo);
        return this.playerOne.equals(playerName) || this.playerTwo.equals(playerName);
    }

    public MoveRespond updateGame(String player, String move) {
        System.out.println("\nCurrent game turn before move: " + this.gameCore.getTurn() + "\n");
        uiController.showBoard(this.gameCore.getBoard());
        System.out.println("Player received in GameSession.class: " + player);
        System.out.println("Move received in GameSession.class: " + move);
        var turn = this.getGameTurnOnPlayer(player);
        this.gameCore.makeMove(move, turn);
        System.out.println("Turn in updateGame: " + turn);
        var respond = this.gameCore.getLastMove();
        System.out.println("\nCurrent game turn after move: " + this.gameCore.getTurn() + "\n");
        uiController.showBoard(this.gameCore.getBoard());
        return respond;
    }
}
