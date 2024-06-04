package sk.tuke.gamestudio.database.interfaces;

import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.server.dto.RegistrationRequest;

import java.util.List;

public interface PlayerService {

    Player logIn(@NotNull RegistrationRequest registrationRequest);

    String checkForExistingPlayer(@NotNull String nickname);

    Player registerNew(@NotNull RegistrationRequest registrationRequest);

    List<Player> getTopTenPlayers();

}
