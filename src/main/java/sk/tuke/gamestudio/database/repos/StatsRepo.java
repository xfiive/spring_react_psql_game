package sk.tuke.gamestudio.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.entity.checkers.Stats;

@Repository
public interface StatsRepo extends JpaRepository<Stats, Integer> {
    @Query("select s from Stats s join Player p on s.stats_id = :statsId")
    public Stats getStatsById(@Param("statsId") int statsId);
}
