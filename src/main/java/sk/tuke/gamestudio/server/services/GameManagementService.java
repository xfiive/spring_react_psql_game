package sk.tuke.gamestudio.server.services;

import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.game.GameSession;
import sk.tuke.gamestudio.server.dto.MoveRespond;

@Service
public class GameManagementService {
    private GameSession currentGameSession = null;


    public String resetPlayers(String player1Name, String player2Name) {
        if (this.currentGameSession != null) {
            this.currentGameSession.resetPlayers(player1Name, player2Name);
            return "true";
        }
        return "false";
    }

    public String surrenderForPlayer(String playerNickname) {
        if (this.currentGameSession != null)
            return this.currentGameSession.surrenderForPlayer(playerNickname);
        else
            return "Failed to surrender for player " + playerNickname;
    }

    public String resetGameSession() {
        this.currentGameSession = new GameSession();
        return "Game session was reset";
    }

    public String startNewGameOrReject(String player1Name, String player2Name) {
//        if (currentGameSession == null) {
        currentGameSession = new GameSession(player1Name, player2Name);
        currentGameSession.startGame();
        return "Game started: " + player1Name + " vs " + player2Name;
//        }
//        return "A game session is already in progress.";
    }

    public MoveRespond processMove(String playerName, String move) {
        if (currentGameSession == null) {
            System.out.println("No active game session.");
            return null;
        }
        if (currentGameSession.hasPlayer(playerName)) {
            System.out.println("Session has player: " + playerName);
            return currentGameSession.updateGame(playerName, move);
        }
        System.out.println("Player not found in active session.");
        return null;
    }
}

