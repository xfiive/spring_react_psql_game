package sk.tuke.gamestudio.database.services;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.tuke.gamestudio.annotations.security.Security;
import sk.tuke.gamestudio.database.interfaces.PlayerService;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.entity.checkers.Stats;
import sk.tuke.gamestudio.server.dto.RegistrationRequest;

import java.util.ArrayList;
import java.util.List;

@Table(name = "players")
@SecondaryTable(name = "stats")
@Security(name = "PlayerServiceJPA")
@Transactional
public class PlayerServiceJPA implements PlayerService {

    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    public PlayerServiceJPA() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Player logIn(@NotNull RegistrationRequest registrationRequest) {
        Player player = null;
        try {
            player = this.entityManager.createNamedQuery("Player.getPlayer", Player.class)
                    .setParameter("nickname", registrationRequest.getNickname())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } catch (NoResultException e) {
            return null;
        } finally {
            if (player != null) {
                System.out.println("Password encoded:     " + registrationRequest.getPassword());
                System.out.println("Password has from db: " + player.getPassword_hash());
                if (!this.passwordEncoder.matches(registrationRequest.getPassword(), player.getPassword_hash())) {
                    return null;
                }
            }
        }
        assert player != null;
        Stats stats;
        try {
            stats = this.entityManager.createNamedQuery("Stats.getStats", Stats.class)
                    .setParameter("statsId", player.getStats().getStats_id())
                    .getSingleResult();
            player.setStats(stats);
        } catch (NoResultException e) {
            return null;
        }

        return player;
    }

    @Override
    public String checkForExistingPlayer(@NotNull String nickname) {
        try {
            var player = this.entityManager.createNamedQuery("Player.getPlayer", Player.class)
                    .setParameter("nickname", nickname)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (player == null)
                return "false";
            return "true";
        } catch (NoResultException e) {
            return "false";
        }
    }


    @Override
    public Player registerNew(@NotNull RegistrationRequest registrationRequest) {
        try {
            this.entityManager.createNamedQuery("Player.getPlayer", Player.class).setParameter("nickname", registrationRequest.getNickname()).getSingleResult();
        } catch (NoResultException e) {
            try {
                Stats stats = new Stats();
                stats.setNullAll();
                this.entityManager.persist(stats);
                Player player = new Player();
                player.setNickname(registrationRequest.getNickname());
                player.setPassword(this.passwordEncoder.encode(registrationRequest.getPassword()));
                player.setPassword_hash(this.passwordEncoder.encode(registrationRequest.getPassword()));
                player.setStats(stats);
                this.entityManager.persist(player);
                return player;
            } catch (NoResultException exception) {
                return null;
            }
        }
        return null;
    }


    @Override
    public List<Player> getTopTenPlayers() {
        try {
            return (List<Player>) this.entityManager.createNamedQuery("Player.getTopTen")
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }


}
