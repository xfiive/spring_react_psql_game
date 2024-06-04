package sk.tuke.gamestudio.server.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.database.interfaces.RatingService;
import sk.tuke.gamestudio.entity.gamestudio.Rating;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/rating")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class RatingController implements RatingService {

    private RatingService ratingService;

    @Autowired
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    @PostMapping("/set")
    public ResponseEntity<?> setRating(@RequestBody @NotNull Rating rating) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(String.valueOf(rating.getRatedon()));
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            rating.setRatedon(sqlDate);
            ratingService.setRating(rating);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to update rating: " + e.getMessage());
        }
    }

    @Override
    @GetMapping("/{game}/average")
    public int getAverageRating(@PathVariable String game) {
        return this.ratingService.getAverageRating(game);
    }

    @Override
    @GetMapping("/{game}/{nickname}")
    public int getRating(@PathVariable String game, @PathVariable String nickname) {
        return this.ratingService.getRating(game, nickname);
    }

    @Override
    @DeleteMapping("/reset")
    public void reset() {
        this.ratingService.reset();
    }
}
