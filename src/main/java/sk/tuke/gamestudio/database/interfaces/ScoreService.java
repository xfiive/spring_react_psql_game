package sk.tuke.gamestudio.database.interfaces;

import sk.tuke.gamestudio.entity.gamestudio.Score;

import java.util.List;

public interface ScoreService {
    /**
     * adds a new score to storage
     *
     * @param score score object to be added
     */
    void addScore(Score score);

    public int getScore(String game, String nickname);

    /**
     * Get 5 best (highest) scores from the storage for the game named <code>game</code>
     *
     * @param game name of the game
     * @return list of the (at most) 5 best scores
     */
    List<Score> getTopScores(String game);

    /**
     * deletes all scores in the storage (for all games)
     */
    void reset();

}
