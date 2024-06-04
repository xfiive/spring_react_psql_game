package sk.tuke.gamestudio.server.services;

import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MatchmakingService {
    private Queue<String> playerQueue = new ConcurrentLinkedQueue<>();

    public void addToQueue(String playerNickname) {
        playerQueue.add(playerNickname);
        checkForMatch();
    }

    private void checkForMatch() {
        while (playerQueue.size() >= 2) {
            String playerOne = playerQueue.poll();
            String playerTwo = playerQueue.poll();
        }
    }


    public String findOpponent(String nickname) {
        return "";
    }
}
