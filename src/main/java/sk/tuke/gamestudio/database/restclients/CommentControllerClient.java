package sk.tuke.gamestudio.database.restclients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.database.interfaces.CommentService;
import sk.tuke.gamestudio.entity.gamestudio.Comment;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
public class CommentControllerClient implements CommentService {

    @Value("${remote.server.api}")
    private String url;

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<?> setComment(Comment comment) {
        restTemplate.postForEntity(url + "/comment/set", comment, Comment.class);
        return null;
    }

    @Override
    public List<Comment> getComments(String game) {
        ResponseEntity<Comment[]> responseEntity = restTemplate.getForEntity(url + "/comment/" + game, Comment[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public String getComment(String game, String nickname) {
        var s = url + "/comment/" + game + "/" + nickname;
        return restTemplate.getForObject(s, String.class);
    }


    @Override
    public Date getCommentDate(String game, String nickname) {
        try {
            return restTemplate.getForEntity(url + "/comment/" + game + "/date/" + nickname, Date.class, nickname).getBody();
        } catch (Exception e) {
            return new Date(Calendar.getInstance().getTimeInMillis());
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
