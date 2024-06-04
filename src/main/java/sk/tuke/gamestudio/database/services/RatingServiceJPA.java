package sk.tuke.gamestudio.database.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import sk.tuke.gamestudio.annotations.security.Security;
import sk.tuke.gamestudio.database.interfaces.RatingService;
import sk.tuke.gamestudio.entity.gamestudio.Rating;

@Transactional
@Table(name = "rating")
@Security(name = "RatingServiceJPA")
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ResponseEntity<?> setRating(@NotNull Rating rating) {
        try {
            var query = this.entityManager.createNamedQuery("Rating.getRating", Integer.class)
                    .setParameter("game", rating.getGame())
                    .setParameter("nickname", rating.getPlayer());
            var existingRating = query.getResultStream().findFirst().orElse(null);
            if (existingRating == null) {
                this.entityManager.persist(rating);
            } else {
                this.entityManager.merge(rating);
            }
            this.entityManager.flush();
            return null;
        } catch (Exception e) {
            System.err.println("Failed to insert a new Rating entity. " + e);
            return null;
        }
    }

    @Override
    public int getAverageRating(String game) {
        Double result;
        try {
            result = this.entityManager.createNamedQuery("Rating.getAverage", Double.class)
                    .setParameter("game", game)
                    .getResultStream()
                    .findFirst()
                    .orElse(0.0);
        } catch (NullPointerException e) {
            return 0;
        }
        return result.intValue();
    }

    @Override
    public int getRating(String game, String nickname) {
        try {
            var query = this.entityManager.createNamedQuery("Rating.getRating", Integer.class).setParameter("game", game).setParameter("nickname", nickname);
            return query
                    .getResultStream()
                    .findFirst()
                    .orElse(5);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void reset() {
    }

}
