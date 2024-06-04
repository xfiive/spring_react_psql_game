package sk.tuke.gamestudio.gamecore.parsers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.database.enums.CommandType;
import sk.tuke.gamestudio.server.dto.RegistrationRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    public static final String loggingRegex = "\\b(?:exit|log\\s*in|sign\\s*up)\\b";
    public static final String restartRequestRegex = "(?i)(yes|y|no|n)";
    private static final String moveRegex = "(?i)[a-hA-H]\\s*[1-8]\\s*[a-hA-H]\\s*[1-8]";
    private static final String commandRegex = "(?i)(pause|p|continue|c|show\\sstats|stats|sh\\ss|help|h|new\\sgame|end\\sgame|secret)";

    public static String @NotNull [] getUserInput() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        Pattern movePattern = Pattern.compile(moveRegex);
        Pattern commandPattern = Pattern.compile(commandRegex, Pattern.CASE_INSENSITIVE);
        System.out.print("\nYour input: ");
        userInput = scanner.nextLine().trim();

        Matcher moveMatcher = movePattern.matcher(userInput);
        Matcher commandMatcher = commandPattern.matcher(userInput);
        List<String> parts = new ArrayList<>();

        while (moveMatcher.find()) {
            parts.addAll(Arrays.asList(getMatchedCoordinates(moveMatcher)));
        }

        while (commandMatcher.find()) {
            parts.addAll(Arrays.asList(getMatchedCommands(commandMatcher)));
        }

        moveMatcher.reset();
        commandMatcher.reset();

        return parts.toArray(new String[0]);
    }


    @Contract("_ -> new")
    private static String @NotNull [] getMatchedCommands(@NotNull Matcher commandMatcher) {
        String groupsFound = commandMatcher.group();
        groupsFound = groupsFound.replaceAll("\\s+", "");
        return new String[]{groupsFound};
    }

    @Contract("_ -> new")
    private static String @NotNull [] getMatchedCoordinates(@NotNull Matcher moveMatcher) {
        String groupsFound = moveMatcher.group().toLowerCase();
        String[] parts = groupsFound.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replaceAll("\\s+", "");
        }
        return parts;
    }

    public static @NotNull RegistrationRequest getLoggingData(@NotNull CommandType command) throws IOException {
        Scanner scanner = new Scanner(System.in);
        var data = new RegistrationRequest();
        if (command.equals(CommandType.SIGN_UP)) {
            System.out.println("\033[33mGood, let's create a new account for you.\033[0m");
            System.out.print("\033[33mEnter your login: \033[0m");
            data.setNickname(scanner.nextLine().trim());
            String password;
            do {
                System.out.print("\033[33mEnter your password (at least 8 characters, 1 uppercase letter): \033[0m");
                password = scanner.nextLine().trim();
            } while (!isValidPassword(password));
            data.setPassword(password);
        } else if (command.equals(CommandType.LOG_IN)) {
            System.out.println("\033[33mGood, let's log in into your account\033[0m");
            System.out.print("\033[33mEnter your login: \033[0m");
            data.setNickname(scanner.nextLine().trim());
            System.out.print("\033[33mEnter your password: \033[0m");
            data.setPassword(scanner.nextLine().trim());
        } else {
            throw new IOException("Failed to parse input data");
        }
        return data;
    }

    private static boolean isValidPassword(@NotNull String password) {
        if (password.length() < 8) {
            System.out.println("Password should be at least 8 characters long.");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password should contain at least one uppercase letter.");
            return false;
        }
        if (password.matches("\\d+")) {
            System.out.println("Password should not consist only of digits.");
            return false;
        }
        return true;
    }


    public static String @NotNull [] getRestartRequest() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        Pattern restartRequestPattern = Pattern.compile(restartRequestRegex);
        System.out.print("Your input: ");
        userInput = scanner.nextLine().trim();

        Matcher restartRequestMatcher = restartRequestPattern.matcher(userInput);
        List<String> parts = new ArrayList<>();

        while (restartRequestMatcher.find()) {
            parts.addAll(Arrays.asList(getMatchedCoordinates(restartRequestMatcher)));
        }

        restartRequestMatcher.reset();

        return parts.toArray(new String[0]);
    }

}
