package io.hishab.familygame.controller;

import io.hishab.familygame.exception.GameException;
import io.hishab.familygame.model.CustomResponse;
import io.hishab.familygame.model.Player;
import io.hishab.familygame.service.GameService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@Validated
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    /**
     * Adds a new player to the game.
     *
     * @param player The player to be added.
     * @return ResponseEntity containing success message.
     * @throws MethodArgumentNotValidException if player validation fails.
     */
    @PostMapping("/player")
    public ResponseEntity<CustomResponse> addPlayer(@RequestBody  Player player) throws MethodArgumentNotValidException {
        logger.info("Received player for addition: {}", player);
        String validationError = validatePlayer(player);
        if (validationError != null) {
            throw new GameException("ERR-400", validationError);
        }
        gameService.addPlayer(player);
        CustomResponse response = CustomResponse.builder()
                .result("Ok")
                .message("Player added successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Starts the game after validating the number of players.
     *
     * @return ResponseEntity containing the name and score of the winner.
     */
    @GetMapping("/start")
    public ResponseEntity<String> startGame() {
        gameService.startGame();
        Player winner = gameService.getWinner();
        return ResponseEntity.ok("The winner is: " + winner.getName() + " with a score of " + winner.getScore());
    }

    /**
     * Retrieves the current scores of all players.
     *
     * @return ResponseEntity containing the list of players with their scores.
     */
    @GetMapping("/scores")
    public ResponseEntity<List<Player>> getCurrentScores() {
        List<Player> scores = gameService.getCurrentScores();
        return ResponseEntity.ok(scores);
    }

    // Manual validation method for Player
    private String validatePlayer(Player player) {
        if (player.getName() == null || player.getName().trim().isEmpty()) {
            return "Player name is mandatory";
        }
        if (player.getAge() == null || player.getAge() < 0) {
            return "Player age can not be negative integer";
        }
        return null;
    }
}

