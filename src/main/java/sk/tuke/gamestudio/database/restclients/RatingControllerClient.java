package sk.tuke.gamestudio.database.restclients;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.database.interfaces.RatingService;
import sk.tuke.gamestudio.entity.gamestudio.Rating;

import java.util.Objects;

@Service
public class RatingControllerClient implements RatingService {

    @Value("${remote.server.api}")
    private String url;
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<?> setRating(@NotNull Rating rating) {
        try {
            restTemplate.postForEntity(url + "/rating/set", rating, Rating.class);
            return null;
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to insert new rating");
            return null;
        }
    }

    @Override
    public int getAverageRating(String game) {
        try {
            var result = restTemplate.getForEntity(url + "/rating/" + game + "/average", Integer.class)
                    .getBody();
            return Objects.requireNonNullElse(result, 0);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getRating(String game, String nickname) {
        try {
            return Objects.requireNonNull(restTemplate.getForEntity(url + "/rating/" + game, Rating.class, nickname).getBody()).getRating();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
