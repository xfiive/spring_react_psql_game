package sk.tuke.gamestudio.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoveRespond {
    private String gameTurn;
    private String isMoveValid;
    private String startMoveCoordinate;
    private String endMoveCoordinate;
    private String checkerToRemoveCoordinate;
    private String gameStatus;
    private String gameWonBy;
}
