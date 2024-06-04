package sk.tuke.gamestudio.server.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.database.interfaces.PlayerService;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.server.dto.RegistrationRequest;

import java.util.List;

@RestController
@RequestMapping("/api/player")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PlayerController implements PlayerService {

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    @PostMapping("/checkers/log-in")
    @ResponseStatus(HttpStatus.OK)
    public Player logIn(@RequestBody @NotNull RegistrationRequest registrationRequest) {
        try {
            return this.playerService.logIn(registrationRequest);
        } catch (Exception e) {
            System.err.println("Error in rest controller");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
        }
    }

    @Override
    @GetMapping("/checkers/exists")
    @ResponseStatus(HttpStatus.OK)
    public String checkForExistingPlayer(@NotNull String nickname) {
        try {
            return this.playerService.checkForExistingPlayer(nickname);
        } catch (Exception e) {
            System.err.println("Some shit has happened in player rest controller" + e);
            return "false";
        }
    }

    @Override
    @PostMapping("/checkers/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Player registerNew(@RequestBody @NotNull RegistrationRequest registrationRequest) {
        try {
            return this.playerService.registerNew(registrationRequest);
        } catch (HttpClientErrorException e) {
            System.err.println("Error during registration: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during registration", e);
        }
    }

    @Override
    @GetMapping("/checkers/get-top")
    @ResponseStatus(HttpStatus.OK)
    public List<Player> getTopTenPlayers() {
        return this.playerService.getTopTenPlayers();
    }
}
