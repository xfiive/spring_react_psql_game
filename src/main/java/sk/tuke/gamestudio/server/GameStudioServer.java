package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.tuke.gamestudio.database.interfaces.CommentService;
import sk.tuke.gamestudio.database.interfaces.PlayerService;
import sk.tuke.gamestudio.database.interfaces.RatingService;
import sk.tuke.gamestudio.database.interfaces.ScoreService;
import sk.tuke.gamestudio.database.services.CommentServiceJPA;
import sk.tuke.gamestudio.database.services.PlayerServiceJPA;
import sk.tuke.gamestudio.database.services.RatingServiceJPA;
import sk.tuke.gamestudio.database.services.ScoreServiceJPA;

@SpringBootApplication
@EntityScan(basePackages = "sk.tuke.gamestudio.entity")
@ComponentScan(basePackages = {"sk.tuke.gamestudio.server", "sk.tuke.gamestudio.database.services", "sk.tuke.gamestudio.database.security"})
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerServiceJPA();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

