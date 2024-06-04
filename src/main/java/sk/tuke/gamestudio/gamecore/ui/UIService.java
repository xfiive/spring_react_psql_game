package sk.tuke.gamestudio.gamecore.ui;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.annotations.facade.Renderer;
import sk.tuke.gamestudio.annotations.facade.UIComponent;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.entity.checkers.Stats;
import sk.tuke.gamestudio.entity.gamestudio.Comment;
import sk.tuke.gamestudio.entity.gamestudio.Score;
import sk.tuke.gamestudio.gamecore.enums.GameResult;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.gamecore.models.board.Board;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.gamecore.models.datastructures.Pair;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Renderer
@Service
public class UIService {
    @UIComponent("Menu")
    public void showMenu() {
        System.out.println("\n");
        System.out.println("|-----                            Menu                               -----|");
        System.out.println("|-----             Menu commands: (c)ontinue, show (stats),          -----|");
        System.out.println("|-----                  new game, end game, (p)ause                  -----|");
    }

    @UIComponent("Board")
    public void showBoard(Board board) {
        System.out.println("\n");
        for (int i = 0; i < Board.BOARD_SIZE / 8 + 3; i++) {
            for (int j = 0; j < Board.BOARD_SIZE / 8 + 2; j++) {
                if ((i == 0 || i == Board.BOARD_SIZE / 8 + 1) && (j == 0 || j == Board.BOARD_SIZE / 8 + 1)) {
                    if (j == 0)
                        System.out.print("--*");
                    else
                        System.out.print(" *--");
                } else if (i == 0 || i == Board.BOARD_SIZE / 8 + 1 || i == Board.BOARD_SIZE / 8 + 2) {
                    if (i == 0 || i == Board.BOARD_SIZE / 8 + 1) {
                        System.out.print(" -");
                    } else {
                        System.out.print("   ");
                        for (int y = 0; y < 8; y++) {
                            System.out.print("    " + (char) (y + 65));
                        }
                        System.out.print("\n");
                        return;
                    }
                } else if (j == 0 || j == Board.BOARD_SIZE / 8 + 1) {
                    if (j == Board.BOARD_SIZE / 8 + 1) {
                        System.out.print(" |");
                    } else {
                        System.out.print("\033[32m" + (9 - i) + "\033[0m" + " |");
                    }
                } else {
                    int index = (i - 1) * 8 + (j - 1);
                    System.out.print(" ");
                    Cell cell = board.getCell(index);
                    System.out.print(cell.toString());
                }
                System.out.print("   ");
            }
            if (i != 0 && i != Board.BOARD_SIZE / 8 && i != Board.BOARD_SIZE / 8 + 1)
                System.out.print("\n\n");
            else
                System.out.print("\n");
        }
    }

    private void showPlayer(@NotNull Player player, String comment, final int rating, String commentedOn) {
        Stats stats = player.getStats();
        System.out.println("Wins  total: " + stats.getWinsTotal() + ", wins white: " + stats.getWinsWhite() + ", wins black: " + stats.getWinsBlack());
        System.out.println("Draw  total: " + stats.getDrawsTotal() + ", draws white: " + stats.getDrawsWhite() + ", draws black: " + stats.getDrawsBlack());
        System.out.println("Loses  total: " + stats.getLosesTotal() + ", loses white: " + stats.getLosesWhite() + ", loses black: " + stats.getLosesBlack());
        System.out.println("Rating set by you: " + rating);
        if (comment != null) {
            if (!(comment.equals("Ooopps, no comments yet"))) {
                if (!Objects.equals(commentedOn, ""))
                    System.out.println("Comment set by you: " + comment + " was given on " + commentedOn);
                else
                    System.out.println(comment);
            } else
                System.out.println(comment);
        } else
            System.out.println("Ooopps, no comments yet");
    }

    public void showStats(@NotNull Integer averageRating, @NotNull List<Comment> allTheComments, @NotNull Pair<Player, Player> players, @NotNull Pair<String, String> comments, @NotNull Pair<Date, Date> date, @NotNull Pair<Integer, Integer> ratings, @NotNull List<Score> topScores) {
        System.out.println("\nFirst player: ");
        this.showPlayer(players.getFirst(), comments.getFirst(), ratings.getFirst(), date.getFirst().toString());
        System.out.println("\nSecond player: ");
        this.showPlayer(players.getSecond(), comments.getSecond(), ratings.getSecond(), date.getSecond().toString());
        if (!topScores.isEmpty()) {
            System.out.println("\n|-----                   TOP 10 game scores                          -----|");
            for (var score : topScores) {
                System.out.println("||| " + score.getPlayer() + " : " + score.getPoints() + " was played on " + score.getPlayedOn() + " |||");
            }
            System.out.println("\n");
        } else {
            System.out.println("\n\n|-----                  Ooopps, no scores yet                        -----|\n\n");
        }
        if (!allTheComments.isEmpty()) {
            for (var comment : allTheComments) {
                System.out.println("Player: " + comment.getPlayer() + " and his comment: " + comment.getComment() + " , that was given on " + comment.getCommentedon());
            }
        } else {
            System.out.println("Ooopps, game has no comments given(");
        }
        System.out.println("Game average rating: " + averageRating);
    }

    @UIComponent("GameResults")
    public void showGameResults(@NotNull GameResult gameResult) {
        System.out.println("|-----                      " + "GAME RESULT" + "                              -----|");
        if (gameResult.equals(GameResult.WHITE_WIN))
            System.out.print("|-----                       " + "\033[34m" + "WHITE WON" + "\033[0m" + "                               -----|");
        else {
            if (gameResult.equals(GameResult.BLACK_WIN))
                System.out.print("|-----                       " + "\033[31m" + "BLACK WON" + "\033[0m" + "                               -----|");
            else
                System.out.print("|-----                       " + "\033[95m" + "  DRAW" + "\033[0m" + "                                  -----|");

        }
    }

    @UIComponent("GameRestartRequest")
    public void requestGameRestart() {
        System.out.println("\n");
        System.out.println("|-----            Heeeey, wanna play the game again?                 -----|");
        System.out.println("|-----                  Type (y)es or (n)no                          -----|");
    }

    @UIComponent("GameEnd")
    public void endgameMessage() {
        System.out.println("\n");
        System.out.println("|-----             Thank u both for playing my game                  -----|");
        System.out.println("|-----                  But for now.. bye-bye)                       -----|");
    }

    @UIComponent("GameTurn")
    public void showGameTurn(@NotNull Turn turn) {
        System.out.println("\n");
        System.out.println("|-----                    Current game turn:                         -----|");
        if (turn.equals(Turn.WHITE))
            System.out.println("|-----                           " + "\033[34m" + turn + "\033[0m" + "                               -----|");
        else
            System.out.println("|-----                           " + "\033[31m" + turn + "\033[0m" + "                               -----|");

    }

    @UIComponent("GameRules")
    public void showGameRules() {
        System.out.println("\n");
        System.out.println("|-----                 Okay, okay, game rules                        -----|");
        System.out.println("|-----                 Never played checkers?                        -----|");
        System.out.println("|-----              Really, aren't you kidding?                      -----|");
        System.out.println("|-----  A simple piece can move forward, only one square diagonally  -----|");
        System.out.println("|-----      The King can move diagonally to any free square          -----|");
        System.out.println("|-----   If the next square to an adjacent enemy's piece is empty,   -----|");
        System.out.println("|-----              then the capture is allowed                      -----|");
    }

    @UIComponent("GameReviewRules")
    public void showGameReviewRules() {
        System.out.println("\n");
        System.out.println("|-----              Hurray! Finish and nothing more                  -----|");
        System.out.println("|-----                   No, that's a joke :D                        -----|");
        System.out.println("|----- Now left a review comment and give a rating to my game(1 - 5) -----|");
        System.out.println("|-----          Order: first player, then the second one             -----|");
    }
}
