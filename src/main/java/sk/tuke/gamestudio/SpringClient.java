package sk.tuke.gamestudio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.database.interfaces.CommentService;
import sk.tuke.gamestudio.database.interfaces.PlayerService;
import sk.tuke.gamestudio.database.interfaces.RatingService;
import sk.tuke.gamestudio.database.interfaces.ScoreService;
import sk.tuke.gamestudio.database.restclients.CommentControllerClient;
import sk.tuke.gamestudio.database.restclients.PlayerControllerClient;
import sk.tuke.gamestudio.database.restclients.RatingControllerClient;
import sk.tuke.gamestudio.database.restclients.ScoreControllerClient;
import sk.tuke.gamestudio.gamecore.console.MainController;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = {"sk.tuke.gamestudio.server.*"}))
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runConsole(@Autowired MainController mainController) {
        return s -> mainController.play();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreControllerClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingControllerClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentControllerClient();
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerControllerClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
