package sk.tuke.gamestudio.gamecore.models.moves.enums;

public enum MoveState {
    SUCCESSFUL_WHITE,
    SUCCESSFUL_BLACK,
    FAILED_WHITE,
    FAILED_BLACK,
    NOT_FINISHED_WHITE,
    NOT_FINISHED_BLACK,
    DEFAULT,
    DEFAULT_FAIL,
    DEFAULT_SUCCESS;

    public boolean isSuccessful() {
        return (this.equals(SUCCESSFUL_BLACK) || this.equals(SUCCESSFUL_WHITE) || this.equals(NOT_FINISHED_WHITE) || this.equals(NOT_FINISHED_BLACK) || this.equals(DEFAULT_SUCCESS));
    }
}
