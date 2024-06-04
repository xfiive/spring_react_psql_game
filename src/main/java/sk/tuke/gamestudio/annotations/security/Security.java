package sk.tuke.gamestudio.annotations.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Security {
    String name();

    String description() default "JPA access to DB";

    String[] authors() default {"Mikhail Shytsko"};
}
