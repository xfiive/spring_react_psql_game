package sk.tuke.gamestudio.database.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import sk.tuke.gamestudio.annotations.security.Security;
import sk.tuke.gamestudio.database.interfaces.CommentService;
import sk.tuke.gamestudio.entity.gamestudio.Comment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Transactional
@Table(name = "comment")
@Security(name = "CommentServiceJPA")
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ResponseEntity<?> setComment(@NotNull Comment comment) {
        try {
//            var query = this.entityManager.createNamedQuery("Comment.getComment", Comment.class)
//                    .setParameter("game", comment.getGame())
//                    .setParameter("nickname", comment.getPlayer());
//            var existingRating = query.getResultStream().findFirst().orElse(null);
//            if (existingRating == null) {
            this.entityManager.persist(comment);
//            } else {
//                this.entityManager.merge(comment);
//            }
//            this.entityManager.flush();
            return null;
        } catch (Exception e) {
            System.err.println("Failed to insert a new Comment entity. " + e);
        }
        return null;
    }


    @Override
    public List<Comment> getComments(String game) {
        try {
            return this.entityManager.createNamedQuery("Comment.getAll", Comment.class).setParameter("game", game)
                    .getResultStream()
                    .toList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String getComment(String game, String nickname) {
        try {
            return this.entityManager.createNamedQuery("Comment.getByNickname", String.class).setParameter("game", game).setParameter("nickname", nickname)
                    .getResultStream()
                    .findFirst().orElse("No comments from you were found(");
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Date getCommentDate(String game, String nickname) {
        try {
            return this.entityManager.createNamedQuery("Comment.getCommentDate", Date.class).setParameter("game", game).setParameter("nickname", nickname)
                    .getResultStream()
                    .findFirst().orElse(new Date(Calendar.getInstance().getTimeInMillis()));
        } catch (NoResultException e) {
            return new Date(Calendar.getInstance().getTimeInMillis());
        }
    }

    @Override
    public void reset() {
        try {
            this.entityManager.createNamedQuery("Comment.resetComments").executeUpdate();
        } catch (Exception e) {
            System.err.println("\nFailed to reset comment table.\n");
        }
    }

}
