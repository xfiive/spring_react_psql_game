package sk.tuke.gamestudio.database.restclients;

import jakarta.persistence.NoResultException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.database.interfaces.ScoreService;
import sk.tuke.gamestudio.entity.gamestudio.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ScoreControllerClient implements ScoreService {
    @Value("${remote.server.api}")
    private String url;

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(url + "/score/add", score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) {
        try {
            return Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(url + "/score/" + game, Score[].class).getBody()));
        } catch (HttpClientErrorException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

    @Override
    public int getScore(String game, String nickname) {
        try {
            val result = restTemplate.getForEntity(url + "/score" + game, Integer.class, nickname).getBody();
            return Objects.requireNonNullElse(result, 0);
        } catch (NoResultException | HttpClientErrorException e) {
            return Integer.MAX_VALUE;
        }
    }
}