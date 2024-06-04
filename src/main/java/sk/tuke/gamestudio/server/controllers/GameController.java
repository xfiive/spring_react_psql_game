package sk.tuke.gamestudio.server.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.server.dto.MoveRequest;
import sk.tuke.gamestudio.server.services.GameManagementService;

@RestController
@RequestMapping("/game")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GameController {

    private GameManagementService gameManagementService;

    @Autowired
    public void setGameManagementService(GameManagementService gameManagementService) {
        this.gameManagementService = gameManagementService;
    }

    @GetMapping("/test")
    public String test() {
        return "WORKS";
    }


    @PostMapping("/surrender")
    public ResponseEntity<?> handleSurrender(@RequestParam String player1Name) {
        System.out.println("Surrender request for: " + player1Name);
        try {
            String session = gameManagementService.surrenderForPlayer(player1Name);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing surrender");
        }
    }

    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestParam String player1Name, @RequestParam String player2Name) {
        System.out.println("Player 1: " + player1Name);
        System.out.println("Player 2: " + player2Name);
        String session = gameManagementService.startNewGameOrReject(player1Name, player2Name);
        if (session != null) {
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.badRequest().body("Cannot start a new game");
        }
    }

    @PostMapping("/reset-players")
    public ResponseEntity<?> resetPlayers(@RequestParam String player1Name, @RequestParam String player2Name) {
        System.out.println("Player 1 in reset: " + player1Name);
        System.out.println("Player 2 in reset: " + player2Name);
        String session = gameManagementService.resetPlayers(player1Name, player2Name);
        if (session != null) {
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.badRequest().body("Cannot reset game players");
        }
    }

    @PostMapping("/reset-session")
    public ResponseEntity<?> resetSession() {
        String session = gameManagementService.resetGameSession();
        if (session != null) {
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.badRequest().body("Cannot reset game session");
        }
    }

    @PostMapping("/players")
    public ResponseEntity<?> addPlayers(@RequestParam String player1Name, @RequestParam String player2Name) {
        System.out.println("Player 1 in addPlayers: " + player1Name);
        System.out.println("Player 2 in addPlayers: " + player2Name);
        String response = gameManagementService.startNewGameOrReject(player1Name, player2Name);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Cannot add players into the game session");
        }
    }

    @PostMapping("/move")
    public ResponseEntity<?> makeMove(@RequestBody @NotNull MoveRequest moveRequest) {
        System.out.println("Nickname: " + moveRequest.getPlayer());
        System.out.println("Move: " + moveRequest.getMove());
        var result = gameManagementService.processMove(moveRequest.getPlayer(), moveRequest.getMove());
        System.out.println(moveRequest.toString());
        if (result != null) {
            if (!result.getGameStatus().equals("Error")) {
                System.out.println("MoveRespond is not null and is not equal to error");
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body("MoveRespond class is null.");
            }
        } else {
            System.out.println("MoveRespond is null or it's status is equal to error");
            if (result == null)
                System.out.println("MoveRespond is null");
            if (result != null)
                System.out.println("MoveRespond status is: " + result.getGameStatus());
            return ResponseEntity.badRequest().body("Move cannot be processed");
        }
    }
}
