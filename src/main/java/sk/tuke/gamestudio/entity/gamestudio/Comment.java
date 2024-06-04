package sk.tuke.gamestudio.entity.gamestudio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@NamedQuery(name = "Comment.getAll", query = "select c from Comment c where c.game = :game")
@NamedQuery(name = "Comment.getComment", query = "select c from Comment c where c.game = :game and c.player = :nickname")
@NamedQuery(name = "Comment.getByNickname", query = "SELECT c.comment FROM Comment c WHERE c.game = :game AND c.player = :nickname")
@NamedQuery(name = "Comment.resetComments", query = "delete from Comment")
@NamedQuery(name = "Comment.getCommentDate", query = "select c.commentedon from Comment c where c.game = :game and c.player = :nickname")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private long ident;

    @Column(name = "player")
    private String player;

    @Column(name = "game")
    private String game;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "commentedon", nullable = false)
    private Date commentedon;

    public Comment() {
    }

    public Comment(String player, String game, String comment, Date commentedon) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedon = commentedon;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "player=" + player +
                ", game='" + game + '\'' +
                ", comment='" + comment + '\'' +
                ", commentedon=" + commentedon +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Comment comment1 = (Comment) object;
        return Objects.equals(player, comment1.player) && Objects.equals(game, comment1.game) && Objects.equals(comment, comment1.comment) && Objects.equals(commentedon, comment1.commentedon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, game, comment, commentedon);
    }
}
