package sk.tuke.gamestudio.entity.checkers;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stats")
@NamedQuery(name = "Stats.getStats", query = "select s from Stats s join Player p on s.stats_id = :statsId")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id", nullable = false)
    private int stats_id;

    @Column(name = "wins_total", nullable = false)
    private int winsTotal;

    @Column(name = "wins_white", nullable = false)
    private int winsWhite;

    @Column(name = "loses_total", nullable = false)
    private int losesTotal;

    @Column(name = "loses_white", nullable = false)
    private int losesWhite;

    @Column(name = "draws_total", nullable = false)
    private int drawsTotal;

    @Column(name = "draws_white", nullable = false)
    private int drawsWhite;

    @Column(name = "games_total", nullable = false)
    private int gamesTotal;

    public Stats() {
    }

    public void setNullAll() {
        this.winsTotal = 0;
        this.winsWhite = 0;
        this.losesTotal = 0;
        this.losesWhite = 0;
        this.drawsTotal = 0;
        this.drawsWhite = 0;
    }

    public int getWinsBlack() {
        return this.winsTotal - this.winsWhite;
    }

    public int getDrawsBlack() {
        return this.drawsTotal - this.drawsWhite;
    }

    public int getLosesBlack() {
        return this.losesTotal - this.losesWhite;
    }

    public void increaseGamesTotal() {
        this.gamesTotal += 1;
    }

    public void increaseWinsTotal() {
        this.winsTotal += 1;
    }

    public void increaseWinsWhite() {
        this.winsWhite += 1;
    }

    public void increaseLosesTotal() {
        this.losesTotal += 1;
    }

    public void increaseLosesWhite() {
        this.losesWhite += 1;
    }

    public void increaseDrawsTotal() {
        this.drawsTotal += 1;
    }

    public void increaseDrawsWhite() {
        this.drawsWhite += 1;
    }
}
