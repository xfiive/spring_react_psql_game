package sk.tuke.gamestudio.gamecore.parser;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.gamecore.models.datastructures.Pair;
import sk.tuke.gamestudio.gamecore.parsers.InputParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ParserTest {

    @Test
    void testGetUserInputMethodWithMove() {
        String input = "A 1 B 2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String[] expected = {"a", "1", "b", "2"};
        assertArrayEquals(expected, InputParser.getUserInput());
    }

    @Test
    void testGetUserInputMethodWithCommand() {
        String input = "pause\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String[] expected = {"pause"};
        assertArrayEquals(expected, InputParser.getUserInput());
    }

    @Test
    void testGetLoggingDataMethodForSignUp() {
        String input = "test_login\ntest_password\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Pair<String, String> expected = new Pair<>("test_login", "test_password");
        assertNotEquals(expected, InputParser.getLoggingData("sign up"));
    }

    @Test
    void testGetLoggingDataMethodForLogIn() {
        String input = "test_login\ntest_password\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Pair<String, String> expected = new Pair<>("test_login", "test_password");
        assertNotEquals(expected, InputParser.getLoggingData("log in"));
    }

    @Test
    void testGetRestartRequestMethod() {
        String input = "yes\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String[] expected = {"yes"};
        assertArrayEquals(expected, InputParser.getRestartRequest());
    }

    @Test
    void testGetRestartRequestMethodWithDifferentCasing() {
        String input = "No\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String[] expected = {"No"};
        expected = Arrays.stream(expected).map(String::toLowerCase).toArray(String[]::new);
        String[] actual = InputParser.getRestartRequest();
        actual = Arrays.stream(actual).map(String::toLowerCase).toArray(String[]::new);
        assertArrayEquals(expected, actual);
    }
}
