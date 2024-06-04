package sk.tuke.gamestudio.entity.gamestudio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@NamedQuery(name = "Rating.getAverage", query = "select avg(r.rating) from Rating r where r.game = :game")
@NamedQuery(name = "Rating.getRating", query = "select r.rating from Rating r where r.game = :game and r.player = :nickname")
@NamedQuery(name = "Rating.insertNew", query = "UPDATE Rating r " +
        "SET r.rating = :rating, r.ratedon = :ratedon " +
        "WHERE r.player = :player AND r.game = :game " +
        "OR NOT EXISTS (SELECT 1 FROM Rating r2 WHERE r2.player = :player AND r2.game = :game)")
@Table(name = "RATING", uniqueConstraints = {@UniqueConstraint(columnNames = {"player", "game"})})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private long ident;

    @Column(name = "player")
    private String player;

    @Column(name = "game")
    private String game;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "ratedon", nullable = false)
    private Date ratedon;

    public Rating() {
    }

    public Rating(String player, String game, int rating, Date ratedon) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedon = ratedon;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "player=" + player +
                ", game='" + game + '\'' +
                ", rating=" + rating +
                ", ratedon=" + ratedon +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Rating rating1 = (Rating) object;
        return rating == rating1.rating && Objects.equals(player, rating1.player) && Objects.equals(game, rating1.game) && Objects.equals(ratedon, rating1.ratedon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, game, rating, ratedon);
    }
}
