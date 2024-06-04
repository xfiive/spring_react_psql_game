package sk.tuke.gamestudio.gamecore.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.gamecore.controllers.services.UsersService;
import sk.tuke.gamestudio.gamecore.enums.GameResult;
import sk.tuke.gamestudio.gamecore.models.datastructures.Pair;

import java.security.NoSuchAlgorithmException;

@Getter
@Setter
@Service
public class UsersController {

    private UsersService usersService;

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void enterUserAccounts(Pair<Player, Player> players) throws NoSuchAlgorithmException {
        this.usersService.enterUsersAccounts(players);
    }

    public void getFinalUsersReviewAndFinishGame(GameResult gameResult, Pair<Player, Player> players) {
        this.usersService.getFinalUsersReviewAndFinishGame(gameResult, players);
    }
}
