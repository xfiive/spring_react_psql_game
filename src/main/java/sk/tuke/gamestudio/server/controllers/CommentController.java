package sk.tuke.gamestudio.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.database.interfaces.CommentService;
import sk.tuke.gamestudio.entity.gamestudio.Comment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CommentController implements CommentService {
    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    @PostMapping("/set")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> setComment(@RequestBody Comment comment) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(String.valueOf(comment.getCommentedon()));
            comment.setCommentedon(new Date(parsedDate.getTime()));

            commentService.setComment(comment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add comment");
        }
    }

    @Override
    @GetMapping("/{game}")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getComments(@PathVariable String game) {
        return this.commentService.getComments(game);
    }

    @Override
    @GetMapping("/{game}/{nickname}")
    @ResponseStatus(HttpStatus.OK)
    public String getComment(@PathVariable String game, @PathVariable String nickname) {
        System.out.println(game + " " + nickname);
        return this.commentService.getComment(game, nickname);
    }

    @Override
    @GetMapping("/{game}/date/{nickname}")
    public Date getCommentDate(@PathVariable String game, @PathVariable String nickname) {
        try {
            return this.commentService.getCommentDate(game, nickname);
        } catch (Exception e) {
            return new Date(Calendar.getInstance().getTimeInMillis());
        }
    }

    @GetMapping("/{game}/date/{nickname}/string")
    public String getDateString(@PathVariable String game, @PathVariable String nickname) {
        try {
            return this.commentService.getCommentDate(game, nickname).toLocalDate().toString();
        } catch (Exception e) {
            return new Date(Calendar.getInstance().getTimeInMillis()).toLocalDate().toString();
        }
    }

    @Override
    @DeleteMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public void reset() {
        this.commentService.reset();
    }
}
