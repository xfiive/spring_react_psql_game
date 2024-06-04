package sk.tuke.gamestudio.gamecore.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.gamecore.controllers.UIController;
import sk.tuke.gamestudio.gamecore.enums.GameResult;
import sk.tuke.gamestudio.gamecore.enums.GameState;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.gamecore.models.board.Board;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CellState;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.datastructures.Pair;
import sk.tuke.gamestudio.gamecore.models.moves.Move;
import sk.tuke.gamestudio.gamecore.models.moves.enums.MoveState;
import sk.tuke.gamestudio.server.dto.MoveRespond;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.System.exit;

@Service
@Getter
public class Core {

    private Map<String, Integer> validCells;

    @Setter
    private MoveRespond lastMove;

    @Setter
    private Turn turn;

    @Setter
    private GameState state;

    private Board board;

    @Setter
    private Pair<Player, Player> players;

    private UIController ui;

    @Setter
    private GameResult gameResult;

    public void setBoard(Board board) {
        this.board = board;
    }

    @Autowired
    public void setPlayers(Pair<Player, Player> players) {
        this.players = players;
    }

    @Autowired
    public void setUi(UIController ui) {
        this.ui = ui;
    }


    public void init() {
        this.players = new Pair<>(null, null);
        this.lastMove = new MoveRespond();
        this.board = new Board();
        this.board.initBoard();
        this.players.setFirst(new Player());
        this.players.setSecond(null);
        this.gameResult = GameResult.UNFINISHED;
        this.validCells = new HashMap<>();
        this.state = GameState.RUNNING;
        this.turn = Turn.WHITE;
        this.initValidCells();
    }

    public boolean isGameFinished(final Turn turn, @NotNull Board board) {
        if (board.checkersAreOut(CheckerColor.WHITE)) {
            this.gameResult = GameResult.BLACK_WIN;
            return true;
        }
        if (board.checkersAreOut(CheckerColor.BLACK)) {
            this.gameResult = GameResult.WHITE_WIN;
            return true;
        }
        boolean hasPossibleMovesWhite = checkForPossibleMoves(CheckerColor.WHITE, board);
        boolean hasPossibleMovesBlack = checkForPossibleMoves(CheckerColor.BLACK, board);
        if (!hasPossibleMovesWhite && !hasPossibleMovesBlack) {
            this.gameResult = GameResult.DRAW;
            return true;
        }
        if (turn.equals(Turn.WHITE) && !hasPossibleMovesWhite) {
            this.gameResult = GameResult.WHITE_WIN;
            return true;
        }
        if (turn.equals(Turn.BLACK) && !hasPossibleMovesBlack) {
            this.gameResult = GameResult.BLACK_WIN;
            return true;
        }

        return false;
    }

    public String surrenderForPlayer(String playerNickname) {
        this.state = GameState.FINISHED;
        this.updateStats();
        return "true";
    }

    public Pair<MoveState, Turn> makeMove(String cellsCoordinates, Turn turn) {
        Move move = createMove(cellsCoordinates, turn);
        if (move == null) {
            updateLastMoveIfFailed(cellsCoordinates);
            return null;
        }

        MoveState state = move.execute(this.board.getBoard());

        var isGameFinished = this.isGameFinished(turn, this.board);
        var previousTurnString = this.turnToString();

        this.turn = updateGameTurn(state);

        this.updateLastMove(state, cellsCoordinates, move.getCheckersBeaten(), isGameFinished, previousTurnString);

        return new Pair<>(state, turn);
    }

    private void updateLastMoveIfFailed(String cellsCoordinates) {
        this.lastMove.setGameTurn(this.turnToString());
        this.lastMove.setIsMoveValid("false");
        if (cellsCoordinates != null) {
            this.lastMove.setStartMoveCoordinate("");
            this.lastMove.setEndMoveCoordinate("");
        }
        this.lastMove.setCheckerToRemoveCoordinate("");
        this.lastMove.setGameStatus("");
        this.lastMove.setGameWonBy("undefined");
    }

    private void updateLastMove(@NotNull MoveState state, String cellsCoordinates, Set<String> checkersBeaten, boolean isGameFinished, String previousTurnString) {
        this.lastMove.setGameTurn(this.turnToString());
        this.lastMove.setIsMoveValid(state.isSuccessful() ? "true" : "false");

        if (cellsCoordinates != null) {
            this.lastMove.setStartMoveCoordinate(cellsCoordinates.substring(0, 2));
            this.lastMove.setEndMoveCoordinate(cellsCoordinates.substring(2, 4));
        }

        this.lastMove.setCheckerToRemoveCoordinate(new Gson().toJson(checkersBeaten));

        if (isGameFinished) {
            this.lastMove.setGameStatus("Won");
            this.lastMove.setGameWonBy(previousTurnString);
            this.updateStats();
        } else {
            this.lastMove.setGameStatus("");
        }
    }


    public void updateStats() {
        Player firstPlayerModel = this.getPlayers().getFirst();
        Player secondPlayerModel = this.getPlayers().getSecond();

        if (firstPlayerModel != null && secondPlayerModel != null)
            switch (gameResult) {
                case WHITE_WIN:
                    updatePlayerStats(firstPlayerModel, secondPlayerModel);
                    break;
                case BLACK_WIN:
                    updatePlayerStats(secondPlayerModel, firstPlayerModel);
                    break;
                default:
                    updateDrawStats(firstPlayerModel, secondPlayerModel);
                    break;
            }
    }


    public @Nullable Move createMove(@NotNull String cellsCoordinates, Turn turn) {
        if (cellsCoordinates.length() != 4 && cellsCoordinates.length() != 5) {
            System.out.println("Invalid input format. Expected format: 'a1b2'.");
            return null;
        }

        String startCoordinate = cellsCoordinates.substring(0, 2);
        String endCoordinate = cellsCoordinates.substring(2, 4);

        System.out.println("Start  cell: " + startCoordinate);
        System.out.println("End    cell: " + endCoordinate);
        System.out.println("Valid cells: " + this.validCells.toString());
        System.out.println("Board: " + this.board.getBoard());


        if (!this.validCells.containsKey(startCoordinate) || !this.validCells.containsKey(endCoordinate)) {
            System.out.println("Invalid start or end coordinate!");
            return null;
        }

        Cell startCell = this.board.getCell(this.validCells.get(startCoordinate));
        Cell endCell = this.board.getCell(this.validCells.get(endCoordinate));

        if (startCell.getCellState().equals(CellState.EMPTY)) {
            System.out.println("Empty start cell detected!");
            return null;
        }

        if (endCell.getCellState().equals(CellState.HAS_CHECKER)) {
            System.out.println("End cell row: " + endCell.getRow());
            System.out.println("End cell column: " + endCell.getColumn());
            System.out.println("End cell is already taken!");
            return null;
        }

        if ((turn == Turn.WHITE && startCell.getChecker().getColor() != CheckerColor.WHITE) ||
                (turn == Turn.BLACK && startCell.getChecker().getColor() != CheckerColor.BLACK)) {
            System.out.println("The checker color does not match the player's turn!");
            return null;
        }

        return new Move(startCell, endCell, turn);
    }

    private Turn updateGameTurn(@NotNull MoveState moveState) {
        return switch (moveState) {
            case FAILED_BLACK, SUCCESSFUL_WHITE, NOT_FINISHED_BLACK -> Turn.BLACK;
            case FAILED_WHITE, NOT_FINISHED_WHITE, SUCCESSFUL_BLACK -> Turn.WHITE;
            default -> Turn.UNDEFINED;
        };
    }

    private void updatePlayerStats(@NotNull Player winner, @NotNull Player loser) {
        winner.getStats().increaseWinsTotal();
        winner.getStats().increaseWinsWhite();
        loser.getStats().increaseLosesTotal();
    }

    private void updateDrawStats(@NotNull Player playerModel1, @NotNull Player playerModel2) {
        playerModel1.getStats().increaseDrawsTotal();
        playerModel1.getStats().increaseDrawsWhite();
        playerModel2.getStats().increaseDrawsTotal();
    }

    private boolean checkForPossibleMoves(CheckerColor color, @NotNull Board board) {
        for (var cell : board.getBoard()) {
            if (cell.getCellState().equals(CellState.HAS_CHECKER) && cell.getChecker().getColor().equals(color) && Move.hasValidMoves(board.getBoard(), cell))
                return true;
        }
        return false;
    }

    public String turnToString() {
        if (this.turn.equals(Turn.WHITE))
            return "White";
        if (this.turn.equals(Turn.BLACK))
            return "Black";
        return "Undefined";
    }

    private void initValidCells() {
        try (InputStream inputStream = Core.class.getResourceAsStream("/valid-moves.json")) {
            assert inputStream != null;
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Integer>>() {
                }.getType();
                this.validCells = gson.fromJson(reader, type);
            }
        } catch (IOException e) {
            System.err.println("Cannot find a source file. Check resources directory for 'possible-moves.json' file before running a game");
            exit(0);
        }
    }
}
