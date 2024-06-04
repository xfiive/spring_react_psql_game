package sk.tuke.gamestudio.database.interfaces;

import org.springframework.http.ResponseEntity;
import sk.tuke.gamestudio.entity.gamestudio.Comment;

import java.sql.Date;
import java.util.List;

public interface CommentService {

    ResponseEntity<?> setComment(Comment comment);

    List<Comment> getComments(String game);

    String getComment(String game, String nickname);

    Date getCommentDate(String game, String nickname);

    void reset();
}
