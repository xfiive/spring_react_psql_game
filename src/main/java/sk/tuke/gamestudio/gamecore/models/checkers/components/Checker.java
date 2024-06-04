package sk.tuke.gamestudio.gamecore.models.checkers.components;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.gamestudio.annotations.factory.FactoryProduct;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerType;

@Getter
@Setter
@FactoryProduct("Checker")
public class Checker {

    private static final String whiteCheckerSkin = "\033[34m" + "W" + "\033[0m";
    private static final String blackCheckerSkin = "\033[31m" + "B" + "\033[0m";
    private CheckerColor color;
    private CheckerType type;

    public Checker(CheckerColor color) {
        this.color = color;
        this.type = CheckerType.MAN;
    }

    public Checker() {
    }

    public Checker(CheckerColor color, CheckerType type) {
        this.type = type;
        this.color = color;
    }

    public void makeKing() {
        this.type = CheckerType.KING;
    }

    @Override
    public String toString() {
        if (this.color == CheckerColor.WHITE)
            return whiteCheckerSkin;
        else
            return blackCheckerSkin;
    }
}
