package sk.tuke.gamestudio.gamecore.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.annotations.facade.Renderer;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.entity.gamestudio.Comment;
import sk.tuke.gamestudio.entity.gamestudio.Score;
import sk.tuke.gamestudio.gamecore.enums.GameResult;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.gamecore.models.board.Board;
import sk.tuke.gamestudio.gamecore.models.datastructures.Pair;
import sk.tuke.gamestudio.gamecore.ui.UIService;

import java.sql.Date;
import java.util.List;

@Service
public class UIController {
    public static String FAILED_TO_PAUSE = "\033[31mOoopps, you can't stop the game right now.\033[0m";
    public static String FAILED_TO_CONTINUE = "\033[31mOoopps, you can't continue the game right now.\033[0m";
    public static String FAILED_TO_SHOW_STATS = "\033[31mOoopps, you can't look at your stats during the match.\033[0m";
    //    public static String ATTEMPT_TO_ENTER_SAME_ACCOUNT = "\033[31mOoopps, you can't enter one account for both users at the same time.\033[0m";
    public static String FAILED_TO_RESTART_GAME = "\033[31mOoopps, you it's forbidden to start a new game right now.\033[0m";
    public static String FAILED_TO_GET_INTO_ACCOUNT = "\033[31mOoopps, that was a kind of a wrong command. Please, type sign up, if you want to create a new account, or log in.\033[0m";
    public static String SUCCESS_ON_ENTER_ACCOUNT = "\033[33mGreat, you successfully entered into your account.\033[0m";
    public static String ACCOUNT_ACCESS_LIMIT = "\033[31mSeems like you've been trying to hack another player's account. No-no-no, goodbye, try later ^_^\033[0m";
    public static String PRE_LOG_IN = "\033[33mAlready have an account? Log in! Wanna create a new one? Sign up with it. Enter log in or sign up.\033[0m";
    public static String PRE_LOG_IN_SECOND_USER = "\033[33mSecond user? Okay, bro, already have an account? Log in! Wanna create a new one? Sign up with it. Enter log in or sign up.\033[0m";

    private UIService ui;

    public UIController() {
        this.ui = new UIService();
    }

    @Autowired
    public void setUi(UIService ui) {
        this.ui = ui;
    }

    @Renderer("Menu")
    public void showMenu() {
        ui.showMenu();
    }

    @Renderer("Board")
    public void showBoard(Board board) {
        ui.showBoard(board);
    }

    @Renderer("Stats")
    public void showStats(@NotNull Integer averageRating, @NotNull List<Comment> allTheComments, @NotNull Pair<Player, Player> players, @NotNull Pair<String, String> comments, Pair<Date, Date> date, @NotNull Pair<Integer, Integer> ratings, @NotNull List<Score> topScores) {
        ui.showStats(averageRating, allTheComments, players, comments, date, ratings, topScores);
    }

    @Renderer("GameResults")
    public void showGameResults(GameResult gameResult) {
        ui.showGameResults(gameResult);
    }

    @Renderer("GameRules")
    public void showGameRules() {
        ui.showGameRules();
    }

    @Renderer("GameEndMessage")
    public void showEndgameMessage() {
        ui.endgameMessage();
    }

    @Renderer("GameRestartRequest")
    public void showGameRestartRequest() {
        ui.requestGameRestart();
    }

    @Renderer("GameTurn")
    public void showGameTurn(Turn turn) {
        ui.showGameTurn(turn);
    }

    @Renderer("GameReview")
    public void showGameReviewRules() {
        ui.showGameReviewRules();
    }
}
