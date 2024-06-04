package sk.tuke.gamestudio.entity.checkers;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sk.tuke.gamestudio.gamecore.models.colors.PlayerColor;

@Getter
@Setter
@Entity
@Table(name = "players")
@NamedQuery(name = "Player.getPlayer", query = "select p from Player p where p.nickname = :nickname")
@NamedQuery(name = "Player.getTopTen", query = "select p from Player p join Stats s on s.stats_id = p.stats.stats_id order by s.winsTotal desc limit 10")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private int player_id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password_hash", nullable = false)
    private String password_hash;

    @OneToOne
    @JoinColumn(name = "stats_id", referencedColumnName = "stats_id")
    private Stats stats;

    @Transient
    private String password;

    @Transient
    private PlayerColor color;

    public Player() {
    }

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public Player(PlayerColor color) {
        this.color = color;
    }

    public Player(int id, Stats statsModel, String nickname, String password_hash) {
        this.player_id = id;
        this.stats = statsModel;
        this.nickname = nickname;
        this.password_hash = password_hash;
    }

    public Player(int player_id, String nickname, String password_hash, Stats stats) {
        this.player_id = player_id;
        this.stats = stats;
        this.nickname = nickname;
        this.password_hash = password_hash;
    }
}
