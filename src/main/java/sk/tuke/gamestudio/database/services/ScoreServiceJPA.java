package sk.tuke.gamestudio.database.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.annotations.security.Security;
import sk.tuke.gamestudio.database.interfaces.ScoreService;
import sk.tuke.gamestudio.entity.gamestudio.Score;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Table(name = "score")
@Security(name = "ScoreServiceJPA")
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getScore(String game, String nickname) {
        try {
            var query = this.entityManager.createNamedQuery("Score.getScore", Integer.class).setParameter("nickname", nickname).setParameter("game", game);
            Long totalScore = Long.valueOf(query.getResultStream().findFirst().orElse(0));
            return totalScore.intValue();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public void addScore(@NotNull Score score) {
        try {
//            var query = this.entityManager.createNamedQuery("Score.getScore", Integer.class)
//                    .setParameter("game", score.getGame())
//                    .setParameter("nickname", score.getPlayer());
//            var existingRating = query.getResultStream().findFirst().orElse(null);
//            if (existingRating == null) {
            this.entityManager.persist(score);
//            } else {
//                this.entityManager.merge(score);
//            }
            this.entityManager.flush();
        } catch (Exception e) {
            System.err.println("Failed to insert a new Score entity. " + e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try {
            var scores = this.entityManager.createNamedQuery("Score.getTopScores", Score.class)
                    .setParameter("game", game)
                    .setMaxResults(10)
                    .getResultStream()
                    .toList();
            if (!scores.isEmpty())
                return scores;
            else
                return new ArrayList<>();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void reset() {
        try {
            throw new IOException("No no no, not that time, my boy ^_^");
        } catch (Exception e) {
            System.err.println("\nFailed to reset score table.\n");
        }
    }

}
