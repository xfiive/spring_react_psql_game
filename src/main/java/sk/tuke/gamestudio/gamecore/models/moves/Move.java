package sk.tuke.gamestudio.gamecore.models.moves;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.gamecore.enums.Turn;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Cell;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CellState;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerType;
import sk.tuke.gamestudio.gamecore.models.moves.enums.MoveDirection;
import sk.tuke.gamestudio.gamecore.models.moves.enums.MoveState;
import sk.tuke.gamestudio.gamecore.models.moves.enums.MoveType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import static java.lang.Math.abs;


@Getter
@Setter
public class Move {

    private final Turn turn;

    private final Cell startCell;

    private final Cell endCell;

    private final Set<String> checkersBeaten = new HashSet<>();

    private MoveType type;

    private MoveDirection direction;

    public Move(Cell startCell, Cell endCell, Turn turn) {
        this.startCell = startCell;
        this.endCell = endCell;
        this.turn = turn;
    }

    public static boolean hasValidMoves(ArrayList<Cell> cells, @NotNull Cell currentCell) {
        if (currentCell.getCellState().equals(CellState.EMPTY))
            return false;

        int index = calculateBoardIndex(currentCell);

        boolean upLeftResult = false;
        boolean upRightResult = false;
        boolean downLeftResult = false;
        boolean downRightResult = false;

        if (0 < index - 18)
            upLeftResult = index % 8 != 0 && (!cells.get(index - 9).getCellState().equals(CellState.HAS_CHECKER) || !cells.get(index - 18).getCellState().equals(CellState.HAS_CHECKER)) && calculateBoardIndex(cells.get(index - 9)) % 8 != 0 && index - 9 >= 0;
        if (0 < index - 14)
            upRightResult = (index + 1) % 8 != 0 && (!cells.get(index - 7).getCellState().equals(CellState.HAS_CHECKER) || !cells.get(index - 14).getCellState().equals(CellState.HAS_CHECKER)) && calculateBoardIndex(cells.get(
                    index - 7)) != 0 && index - 7 >= 0;
        if (index + 14 < 63)
            downLeftResult = index % 8 != 0 && (!cells.get(index + 7).getCellState().equals(CellState.HAS_CHECKER) || !cells.get(index + 14).getCellState().equals(CellState.HAS_CHECKER)) && calculateBoardIndex(cells.get(index + 7)) % 8 != 0 &&
                    index + 7 <= 63;
        if (index + 18 < 63)
            downRightResult = (index + 1) % 8 != 0 && (!cells.get(index + 9).getCellState().equals(CellState.HAS_CHECKER) || !cells.get(index + 18).getCellState().equals(CellState.HAS_CHECKER)) && calculateBoardIndex(cells.get(index + 9)) % 8 != 0 &&
                    index + 9 <= 63;

        if (currentCell.getChecker().getColor() == CheckerColor.WHITE) {
            if (currentCell.getChecker().getType().equals(CheckerType.KING)) {
                return upLeftResult || upRightResult || downLeftResult || downRightResult;
            }
            return upLeftResult || upRightResult;
        }

        if (currentCell.getChecker().getType().equals(CheckerType.KING)) {
            return upLeftResult || upRightResult || downLeftResult || downRightResult;
        }

        return downLeftResult || downRightResult;
    }

    public static int calculateBoardIndex(@NotNull Cell currentCell) {
        return currentCell.getRow() * 8 + currentCell.getColumn();
    }

    private @NotNull ArrayList<Integer> getIntermediateIndexes(int startIndex, int endIndex) {
        ArrayList<Integer> intermediateIndexes = new ArrayList<>();
        switch (this.direction) {
            case UP_RIGHT:
                while (7 < startIndex - endIndex) {
                    startIndex -= 7;
                    intermediateIndexes.add(startIndex);
                }
                break;
            case UP_LEFT:
                while (9 < startIndex - endIndex) {
                    startIndex -= 9;
                    intermediateIndexes.add(startIndex);
                }
                break;
            case DOWN_RIGHT:
                while (9 < endIndex - startIndex) {
                    endIndex -= 9;
                    intermediateIndexes.add(endIndex);
                }
                break;
            case DOWN_LEFT:
                while (7 < endIndex - startIndex) {
                    endIndex -= 7;
                    intermediateIndexes.add(endIndex);
                }
                break;
            default:
                break;
        }
        return intermediateIndexes;
    }

    private boolean checkIfHasIntermediateCheckers(ArrayList<Cell> cells, @NotNull ArrayList<Integer> intermediateIndexes) {
        if (intermediateIndexes.isEmpty())
            return false;

        for (Integer index : intermediateIndexes) {
            if (cells.get(index).getCellState() == CellState.HAS_CHECKER)
                return true;
        }
        return false;
    }

    private boolean checkIfHasChecker(@NotNull ArrayList<Cell> cells, int index, int indexModified) {
        return cells.get(index).getCellState().equals(CellState.HAS_CHECKER) && cells.get(indexModified).getCellState().equals(CellState.EMPTY);
    }

    private boolean hasCheckersToTake(ArrayList<Cell> cells, @NotNull Cell currentCell) {
        try {
            if (currentCell.getCellState() == CellState.EMPTY)
                return false;

            int index = Move.calculateBoardIndex(currentCell);

            int upLeftIndex = index - 9;
            int upRightIndex = index - 7;
            int downLeftIndex = index + 7;
            int downRightIndex = index + 9;


            if (currentCell.getChecker().getType() == CheckerType.KING) {
                if (checkIfHasChecker(cells, upLeftIndex, upLeftIndex - 9))
                    return true;
                if (checkIfHasChecker(cells, upRightIndex, upRightIndex - 7))
                    return true;
                if (checkIfHasChecker(cells, downLeftIndex, downLeftIndex + 7))
                    return true;
                return checkIfHasChecker(cells, downRightIndex, downRightIndex + 9);
            }

            // lower  35  &&  upper  17

            System.out.print("\n");
            System.out.println("Current cell row: " + currentCell.getRow());
            System.out.println("Current cell column: " + currentCell.getColumn());
            System.out.println("Index: " + index);
            System.out.println("UpLeftIndex: " + upLeftIndex);
            System.out.println("UpLeftIndex - 9: " + (upLeftIndex - 9));
            System.out.print("\n");

            if (currentCell.getChecker().getColor().equals(CheckerColor.WHITE)) {
                if (checkIfHasChecker(cells, upLeftIndex, upLeftIndex - 9))
                    return true;
                return checkIfHasChecker(cells, upRightIndex, upRightIndex - 7);
            }

            if (checkIfHasChecker(cells, downLeftIndex, downLeftIndex + 7))
                return true;
            return checkIfHasChecker(cells, downRightIndex, downRightIndex + 9);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private int calculateMoveLength() {
        int startIndex = this.startCell.getRow() * 8 + this.startCell.getColumn();
        int endIndex = this.endCell.getRow() * 8 + this.endCell.getColumn();

        if (this.direction == MoveDirection.UP_LEFT || this.direction == MoveDirection.DOWN_RIGHT) {
            return abs(startIndex - endIndex) / 9;
        } else {
            return abs(startIndex - endIndex) / 7;
        }
    }

    public MoveState execute(ArrayList<Cell> cells) {
        if (this.getStartCell().getCellState() == CellState.EMPTY) {
            if (this.turn == Turn.WHITE)
                return MoveState.FAILED_WHITE;
            else
                return MoveState.FAILED_BLACK;
        }

        if (this.getStartCell() == this.getEndCell()) {
            if (this.turn == Turn.WHITE)
                return MoveState.FAILED_WHITE;
            else
                return MoveState.FAILED_BLACK;
        }

        if (this.getEndCell().getCellState() == CellState.HAS_CHECKER) {
            if (this.turn == Turn.WHITE)
                return MoveState.FAILED_WHITE;
            else
                return MoveState.FAILED_BLACK;
        }

        int startIndex = Move.calculateBoardIndex(startCell);
        int endIndex = Move.calculateBoardIndex(endCell);

        if (endIndex < 0 || 63 < endIndex) {
            if (this.turn == Turn.WHITE)
                return MoveState.FAILED_WHITE;
            else
                return MoveState.FAILED_BLACK;
        }

        if (startIndex < endIndex) {
            if ((endIndex - startIndex) % 9 == 0)
                this.setDirection(MoveDirection.DOWN_RIGHT);
            else
                this.setDirection(MoveDirection.DOWN_LEFT);
        } else {
            if ((startIndex - endIndex) % 9 == 0)
                this.setDirection(MoveDirection.UP_LEFT);
            else
                this.setDirection(MoveDirection.UP_RIGHT);
        }

        int checkersAmountBeforeMove = this.countCellsWithCheckers(cells);

        ArrayList<Integer> intermediateIndexes = getIntermediateIndexes(startIndex, endIndex);

        boolean hasIntermediateCheckers = this.checkIfHasIntermediateCheckers(cells, intermediateIndexes);

        if (!hasIntermediateCheckers && this.startCell.getChecker().getType() == CheckerType.MAN && 1 < this.calculateMoveLength()) {
            if (this.turn == Turn.WHITE)
                return MoveState.FAILED_WHITE;
            else
                return MoveState.FAILED_BLACK;
        }

        if (!hasIntermediateCheckers) {
            this.moveChecker();
        } else {
            MoveState jumpResult = this.jumpChecker(cells, intermediateIndexes);
            if (jumpResult != MoveState.SUCCESSFUL_WHITE && jumpResult != MoveState.SUCCESSFUL_BLACK) {
                return jumpResult;
            }
        }

        if (this.countCellsWithCheckers(cells) < checkersAmountBeforeMove) {
            if (this.hasCheckersToTake(cells, endCell)) {
                if (this.turn == Turn.WHITE)
                    return MoveState.NOT_FINISHED_WHITE;
                else
                    return MoveState.NOT_FINISHED_BLACK;
            }
        }

        if (this.turn == Turn.WHITE)
            return MoveState.SUCCESSFUL_WHITE;
        else
            return MoveState.SUCCESSFUL_BLACK;
    }

    @Contract(pure = true)
    private @NotNull String indexToCoordinates(int index) {
        char column = (char) ('a' + (index % 8));
        int row = 8 - (index / 8);
        return "" + column + row;
    }

    private void moveChecker() {
        this.endCell.setChecker(this.startCell.getChecker());
        this.startCell.setCellState(CellState.EMPTY);
        this.endCell.setCellState(CellState.HAS_CHECKER);
        this.startCell.setChecker(null);
    }

    private MoveState jumpChecker(ArrayList<Cell> cells, @NotNull ArrayList<Integer> intermediateIndexes) {
        int emptyCellsInRow = 0;
        boolean checkerFound = false;

        ListIterator<Integer> iteratorFirst = intermediateIndexes.listIterator(intermediateIndexes.size());

        while (iteratorFirst.hasPrevious()) {
            int currentIndex = iteratorFirst.previous();
            Cell intermediateCell = cells.get(currentIndex);
            if (intermediateCell.getCellState() == CellState.HAS_CHECKER) {
                intermediateCell.setCellState(CellState.EMPTY);
                intermediateCell.setChecker(null);
                this.checkersBeaten.add(indexToCoordinates(currentIndex));
                break;
            }
        }

        for (Integer index : intermediateIndexes) {
            Cell cell = cells.get(index);

            if (checkerFound && cell.getCellState() == CellState.HAS_CHECKER) {
                if (this.turn == Turn.WHITE)
                    return MoveState.FAILED_WHITE;
                else
                    return MoveState.FAILED_BLACK;
            } else
                emptyCellsInRow += 1;

            if (emptyCellsInRow == 2) {
                if (this.turn == Turn.WHITE)
                    return MoveState.FAILED_WHITE;
                else
                    return MoveState.FAILED_BLACK;
            }

            if (cell.getCellState() == CellState.HAS_CHECKER)
                checkerFound = true;
        }

        ListIterator<Integer> iterator = intermediateIndexes.listIterator(intermediateIndexes.size());

        while (iterator.hasPrevious()) {
            Cell intermediateCell = cells.get(iterator.previous());
            if (intermediateCell.getCellState() == CellState.HAS_CHECKER) {
                intermediateCell.setCellState(CellState.EMPTY);
                intermediateCell.setChecker(null);
                break;
            }
        }

        this.endCell.setChecker(this.startCell.getChecker());
        this.endCell.setCellState(CellState.HAS_CHECKER);
        this.startCell.setCellState(CellState.EMPTY);
        this.startCell.setChecker(null);

        if (this.turn == Turn.WHITE)
            return MoveState.SUCCESSFUL_WHITE;
        else
            return MoveState.SUCCESSFUL_BLACK;
    }

    @Contract(pure = true)
    private int countCellsWithCheckers(@NotNull ArrayList<Cell> cells) {
        int count = 0;
        for (Cell cell : cells) {
            if (cell.getCellState() == CellState.HAS_CHECKER) {
                count += 1;
            }
        }
        return count;
    }

    public void setType(MoveType type) {
        this.type = type;
    }

    public void setDirection(MoveDirection direction) {
        this.direction = direction;
    }
}
