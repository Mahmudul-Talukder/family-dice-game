package io.hishab.familygame.controller;

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

    @PostMapping("/player")
    public ResponseEntity<CustomResponse> addPlayer(@RequestBody @Valid Player player) throws MethodArgumentNotValidException {
        logger.info("Received player for addition: {}", player);
        gameService.addPlayer(player);
        CustomResponse response = CustomResponse.builder()
                .result("Ok")
                .message("Player added successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Starts the game after validating that the number of players is between 2 and 4.
     *
     * @return ResponseEntity containing the name of the winning player.
     */
    @GetMapping("/start")
    public ResponseEntity<String> startGame() {
        gameService.startGame();
        Player winner = gameService.getWinner();
        return ResponseEntity.ok("The winner is: " + winner.getName() + " with a score of " + winner.getScore());
    }

    @GetMapping("/scores")
    public ResponseEntity<List<Player>> getCurrentScores() {
        List<Player> scores = gameService.getCurrentScores();
        return ResponseEntity.ok(scores);
    }
}
