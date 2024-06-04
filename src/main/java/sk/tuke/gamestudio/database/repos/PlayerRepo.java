package sk.tuke.gamestudio.database.repos;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.entity.checkers.Player;

import java.util.List;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Integer> {
    @Query("select p from Player p where p.nickname = :nickname")
    List<Player> getByName(@Param("nickname") String nickname);

    @Query("select p from Player p join Stats s on s.stats_id = p.stats.stats_id order by s.winsTotal desc limit 10")
    List<Player> getTopTen();

    @Transactional
    @Query(value = "INSERT INTO stats (wins_total, wins_white, loses_total, loses_white, draws_total, draws_white, games_total) VALUES (0, 0, 0, 0, 0, 0, 0) RETURNING stats_id", nativeQuery = true)
    int insertStatsAndGetId();

    @Transactional
    @Query(value = "INSERT INTO players (stats_id, nickname, password_hash) VALUES (:statsId, :nickname, :passwordHash) RETURNING player_id", nativeQuery = true)
    int insertPlayer(@Param("statsId") int statsId, @Param("nickname") String nickname, @Param("passwordHash") String passwordHash);

    default void insertNew(String nickname, String passwordHash) {
        int statsId = insertStatsAndGetId();
        insertPlayer(statsId, nickname, passwordHash);
    }
}
