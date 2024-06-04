package sk.tuke.gamestudio.database.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import sk.tuke.gamestudio.entity.gamestudio.Rating;

public interface RatingService {
    ResponseEntity<?> setRating(@NotNull Rating rating);

    int getAverageRating(String game);

    int getRating(String game, String nickname);

    void reset();

}
