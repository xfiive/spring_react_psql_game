package sk.tuke.gamestudio.gamecore.models.checkers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.gamecore.models.checkers.components.Checker;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerColor;
import sk.tuke.gamestudio.gamecore.models.checkers.enums.CheckerType;
import sk.tuke.gamestudio.annotations.factory.FactoryPattern;
import sk.tuke.gamestudio.annotations.factory.FactoryProduct;

@FactoryPattern("Checker Factory")
public class CheckerFactory {

    @Contract(value = "_ -> new", pure = true)
    @FactoryProduct("Checker")
    public static @NotNull Checker createChecker(CheckerColor color) {
        return new Checker(color);
    }

    @FactoryProduct("Checker")
    public static @NotNull Checker createChecker(CheckerColor color, CheckerType type) {
        Checker checker = new Checker(color);
        checker.setType(type);
        return checker;
    }
}
