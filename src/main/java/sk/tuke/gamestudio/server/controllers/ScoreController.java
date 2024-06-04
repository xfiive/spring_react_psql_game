package sk.tuke.gamestudio.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.database.interfaces.ScoreService;
import sk.tuke.gamestudio.entity.gamestudio.Score;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/score")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ScoreController implements ScoreService {

    private ScoreService scoreService;

    @Autowired
    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/{game}")
    @Override
    public List<Score> getTopScores(@PathVariable String game) {
        return scoreService.getTopScores(game);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not available via webservice");
    }

    @Override
    @PostMapping("/add")
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score);
    }

    @Override
    @GetMapping("/{game}/{nickname}")
    public int getScore(@PathVariable String game, @PathVariable String nickname) {
        return scoreService.getScore(game, nickname);
    }
}
