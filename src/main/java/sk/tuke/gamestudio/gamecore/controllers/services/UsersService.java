package sk.tuke.gamestudio.gamecore.controllers.services;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import sk.tuke.gamestudio.database.enums.CommandType;
import sk.tuke.gamestudio.database.enums.PlayerToSet;
import sk.tuke.gamestudio.database.interfaces.CommentService;
import sk.tuke.gamestudio.database.interfaces.PlayerService;
import sk.tuke.gamestudio.database.interfaces.RatingService;
import sk.tuke.gamestudio.database.interfaces.ScoreService;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.entity.gamestudio.Comment;
import sk.tuke.gamestudio.entity.gamestudio.Rating;
import sk.tuke.gamestudio.entity.gamestudio.Score;
import sk.tuke.gamestudio.gamecore.controllers.UIController;
import sk.tuke.gamestudio.gamecore.enums.GameResult;
import sk.tuke.gamestudio.gamecore.models.colors.PlayerColor;
import sk.tuke.gamestudio.gamecore.models.datastructures.Pair;
import sk.tuke.gamestudio.gamecore.parsers.InputParser;
import sk.tuke.gamestudio.server.dto.RegistrationRequest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

@Getter
@Setter
@Service
public class UsersService {

    public static final String GAME_TO_INSERT = "checkers";

    private UIController ui;

    private PlayerService playerService;

    private CommentService commentService;

    private ScoreService scoreService;

    private RatingService ratingService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Autowired
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setUi(UIController uiController) {
        this.ui = uiController;
    }

    public void enterUsersAccounts(Pair<Player, Player> players) throws NoSuchAlgorithmException {
        this.getIntoAccount(UIController.PRE_LOG_IN, PlayerToSet.FIRST_PLAYER, players);
        System.out.println(UIController.SUCCESS_ON_ENTER_ACCOUNT);
        this.getIntoAccount(UIController.PRE_LOG_IN_SECOND_USER, PlayerToSet.SECOND_PLAYER, players);
        System.out.println(UIController.SUCCESS_ON_ENTER_ACCOUNT);
    }

    public void showStats(@NotNull Pair<Player, Player> players) {
        var firstPlayerComment = this.commentService.getComment(UsersService.GAME_TO_INSERT, players.getFirst().getNickname());
        var secondPlayerComment = this.commentService.getComment(UsersService.GAME_TO_INSERT, players.getSecond().getNickname());

        if (firstPlayerComment == null)
            System.out.println("first comment is null");
        if (secondPlayerComment == null)
            System.out.println("second comment is null");

        Pair<String, String> comments = new Pair<>();
        comments.setFirst(firstPlayerComment);
        comments.setSecond(secondPlayerComment);

        var firstPlayerCommentDate = this.commentService.getCommentDate(UsersService.GAME_TO_INSERT, players.getFirst().getNickname());
        var secondPlayerCommentDate = this.commentService.getCommentDate(UsersService.GAME_TO_INSERT, players.getSecond().getNickname());

        Pair<Date, Date> date = new Pair<>();
        date.setFirst(firstPlayerCommentDate);
        date.setSecond(secondPlayerCommentDate);

        var topScores = this.scoreService.getTopScores(UsersService.GAME_TO_INSERT);

        var firstPlayerRating = this.ratingService.getRating(UsersService.GAME_TO_INSERT, players.getFirst().getNickname());
        var secondPlayerRating = this.ratingService.getRating(UsersService.GAME_TO_INSERT, players.getFirst().getNickname());

        Pair<Integer, Integer> ratings = new Pair<>();
        ratings.setFirst(firstPlayerRating);
        ratings.setSecond(secondPlayerRating);

        var allTheComments = this.commentService.getComments(GAME_TO_INSERT);
        var averageRating = this.ratingService.getAverageRating(GAME_TO_INSERT);

        this.ui.showStats(averageRating, allTheComments, players, comments, date, ratings, topScores);
    }

    private void getIntoAccount(String loggingMessage, PlayerToSet playerToSet, Pair<Player, Player> players) throws NoSuchAlgorithmException {
        System.out.println(loggingMessage);
        int attemptsCount = 0;
        while (attemptsCount < 3) {
            CommandType commandType = getCommandType();
            if (commandType.equals(CommandType.LOG_IN)) {
                if (this.tryLogIn(playerToSet, players, commandType))
                    return;
            }
            if (commandType.equals(CommandType.SIGN_UP)) {
                if (this.trySignUp(playerToSet, players, commandType))
                    return;
            }
            System.out.println(loggingMessage);
            attemptsCount += 1;
        }
        System.err.println(UIController.ACCOUNT_ACCESS_LIMIT);
        exit(0);
    }

    private boolean tryLogIn(PlayerToSet playerToSet, Pair<Player, Player> players, CommandType commandType) {
        val data = this.getUserAccountData(commandType);
        assert data != null;
        Player playerModel;
        playerModel = this.playerService.logIn(data);
        if (playerModel == null) {
            System.out.println("\033[31mOoopps, something has obviously gone wrong. Invalid login or password, try again.\033[0m");
            return false;
        }
        if (playerToSet.equals(PlayerToSet.SECOND_PLAYER) && Objects.equals(players.getFirst().getNickname(), playerModel.getNickname())) {
            System.out.println("\033[31mOoopps, something has obviously gone wrong. We probably have such an account already been entered\033[0m");
            return false;
        }
        this.setUpPlayer(playerModel, players, playerToSet);
        return true;
    }

    private boolean trySignUp(PlayerToSet playerToSet, Pair<Player, Player> players, CommandType commandType) {
        val requestData = this.getUserAccountData(commandType);
        assert requestData != null;
        Player playerModel;
        playerModel = this.playerService.registerNew(requestData);
        if (playerModel == null) {
            System.out.println("\033[31mOoopps, something has obviously gone wrong. We probably have such an account already been registered\033[0m");
            return false;
        }
        this.setUpPlayer(playerModel, players, playerToSet);
        return true;
    }

    private void setUpPlayer(Player playerModel, Pair<Player, Player> players, @NotNull PlayerToSet playerToSet) {
        if (playerToSet.equals(PlayerToSet.FIRST_PLAYER))
            players.setFirst(playerModel);
        else
            players.setSecond(playerModel);
    }

    private @Nullable RegistrationRequest getUserAccountData(CommandType commandType) {
        var request = new RegistrationRequest();
        try {
            request = InputParser.getLoggingData(commandType);
        } catch (IOException ioException) {
            System.out.println(UIController.FAILED_TO_GET_INTO_ACCOUNT);
            return null;
        }
        return request;
    }

    private @NotNull CommandType getCommandType() {
        System.out.print("Your input: ");

        Scanner scanner = new Scanner(System.in);
        String userInput;
        Pattern loggingPattern = Pattern.compile(InputParser.loggingRegex, Pattern.CASE_INSENSITIVE);
        userInput = scanner.nextLine().toLowerCase().trim();
        Matcher loggingMatcher = loggingPattern.matcher(userInput);

        if (!loggingMatcher.matches()) {
            System.out.println(UIController.FAILED_TO_GET_INTO_ACCOUNT);
            return CommandType.UNDEFINED;
        }

        String command = loggingMatcher.group().trim().toLowerCase();
        return switch (command) {
            case "sign up" -> CommandType.SIGN_UP;
            case "log in" -> CommandType.LOG_IN;
            default -> CommandType.UNDEFINED;
        };
    }

    // console + input handler
    public void getFinalUsersReviewAndFinishGame(GameResult gameResult, @NotNull Pair<Player, Player> players) {
        this.ui.showGameReviewRules();
        Pair<String, Integer> reviewFirstPlayer = new Pair<>();
        Pair<String, Integer> reviewSecondPlayer = new Pair<>();

        Scanner scanner = new Scanner(System.in);

        System.out.println("|-----         First player, enter your comment, please              -----|");
        reviewFirstPlayer.setFirst(scanner.next().trim());
        System.out.println("|-----         First player, enter your rating,  please              -----|");
        reviewFirstPlayer.setSecond(this.getGameRating());
        System.out.println("|-----         Second player, enter your comment, please             -----|");
        reviewSecondPlayer.setFirst(scanner.next().trim());
        System.out.println("|-----         Second player, enter your rating,  please             -----|");
        reviewSecondPlayer.setSecond(this.getGameRating());

        Pair<Pair<String, Integer>, Pair<String, Integer>> users = new Pair<>();
        users.setFirst(reviewFirstPlayer);
        users.setSecond(reviewSecondPlayer);

        this.receiveUsersReview(gameResult, users, players);
        this.ui.showEndgameMessage();

        exit(0);
    }

    public int getGameRating() {
        int rating = 0;
        boolean flagFirstIter = false;
        var scanner = new Scanner(System.in);
        System.out.println("|-----                    Now enter your rating                      -----|");
        do {
            String input = scanner.nextLine();
            try {
                rating = Integer.parseInt(input);
                if (rating < 1 || rating > 5) {
                    System.out.println("|-----         No-no-no,  it must be from 1 to 5, try again          -----|");
                    rating = 0;
                }
            } catch (NumberFormatException e) {
                if (flagFirstIter) {
                    System.out.println("|-----         No-no-no, that was an invalid one, try again          -----|");
                    rating = 0;
                } else {
                    flagFirstIter = true;
                }
            }
        } while (rating < 1 || rating > 5);

        return rating;
    }

    public Pair<Integer, Integer> getGameScore(@NotNull GameResult gameResult, Pair<Player, Player> players) {
        Pair<Integer, Integer> score = new Pair<>();
        switch (gameResult) {
            case DRAW:
                score.setFirst(1);
                score.setSecond(1);
                break;
            case WHITE_WIN:
                if (players.getFirst().getColor().equals(PlayerColor.WHITE)) {
                    score.setFirst(3);
                    score.setSecond(-1);
                } else {
                    score.setFirst(-1);
                    score.setSecond(3);
                }
                break;
            case BLACK_WIN:
                if (players.getFirst().getColor().equals(PlayerColor.BLACK)) {
                    score.setFirst(3);
                    score.setSecond(-1);
                } else {
                    score.setFirst(-1);
                    score.setSecond(3);
                }
                break;
            default:
                score.setFirst(0);
                score.setSecond(0);
                break;
        }

        int scoreFirstPlayer = this.scoreService.getScore("checkers", players.getFirst().getNickname());
        int scoreSecondPlayer = this.scoreService.getScore("checkers", players.getSecond().getNickname());

        if (scoreFirstPlayer < 0 && score.getFirst() < 0)
            score.setFirst(0);
        if (scoreSecondPlayer < 0 && score.getSecond() < 0)
            score.setSecond(0);

        return score;
    }

    private void receiveUsersReview(@RequestBody GameResult gameResult, @RequestBody @NotNull Pair<Pair<String, Integer>, Pair<String, Integer>> playersReviews, @RequestBody @NotNull Pair<Player, Player> players) {
        Date javaCurrentDate = new Date(Calendar.getInstance().getTimeInMillis());
        Date sqlCurrentDate = new Date(javaCurrentDate.getTime());

        Comment firstPlayerComment = new Comment(players.getFirst().getNickname(), GAME_TO_INSERT, playersReviews.getFirst().getFirst(), sqlCurrentDate);
        Comment secondPlayerComment = new Comment(players.getSecond().getNickname(), GAME_TO_INSERT, playersReviews.getSecond().getFirst(), sqlCurrentDate);

        Rating firstPlayerRating = new Rating(players.getFirst().getNickname(), GAME_TO_INSERT, playersReviews.getFirst().getSecond(), sqlCurrentDate);
        Rating secondPlayerRating = new Rating(players.getSecond().getNickname(), GAME_TO_INSERT, playersReviews.getSecond().getSecond(), sqlCurrentDate);

        Pair<Integer, Integer> score = this.getGameScore(gameResult, players);

        Score firstPlayerScore = new Score(players.getSecond().getNickname(), GAME_TO_INSERT, score.getFirst(), sqlCurrentDate);
        Score secondPlayerScore = new Score(players.getSecond().getNickname(), GAME_TO_INSERT, score.getSecond(), sqlCurrentDate);

        this.commentService.setComment(firstPlayerComment);
        this.commentService.setComment(secondPlayerComment);
        this.ratingService.setRating(firstPlayerRating);
        this.ratingService.setRating(secondPlayerRating);
        this.scoreService.addScore(firstPlayerScore);
        this.scoreService.addScore(secondPlayerScore);
    }

}
