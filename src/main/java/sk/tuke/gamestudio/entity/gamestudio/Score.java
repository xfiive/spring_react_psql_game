package sk.tuke.gamestudio.entity.gamestudio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@NamedQuery(name = "Score.getTopScores", query = "select s from Score s where s.game = :game order by s.points desc")
@NamedQuery(name = "Score.resetScores", query = "DELETE FROM Score")
@NamedQuery(name = "Score.getScore", query = "select s.points from Score s where s.player = :nickname and s.game = :game")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int ident;

    @Column(name = "player", nullable = false)
    private String player;

    @Column
    private String game;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "playedon", nullable = false)
    private Date playedOn;

    public Score() {
    }

    public Score(String player, String game, int points, Date playedOn) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", points=" + points +
                ", playedOn=" + playedOn +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Score score = (Score) object;
        return points == score.points && Objects.equals(player, score.player) && Objects.equals(game, score.game) && Objects.equals(playedOn, score.playedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, game, points, playedOn);
    }
}


