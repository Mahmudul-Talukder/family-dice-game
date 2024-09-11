package io.hishab.familygame.service.impl;

import io.hishab.familygame.config.GameProperties;
import io.hishab.familygame.exception.GameException;
import io.hishab.familygame.model.Player;
import io.hishab.familygame.service.GameService;
import io.hishab.familygame.util.DiceRoller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameProperties gameProperties;
    private final RestTemplate restTemplate;
    private final DiceRoller diceRoller;

    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private boolean gameStarted = false;
    private Player winner = null;
    private final Random random = new Random();

    @Override
    public void addPlayer(Player player) throws GameException {
        if (gameStarted) {
            throw new GameException("err-201", "Game has already started. Cannot add new players.");
        }
        if (players.size() >= 4) {
            throw new GameException("err-202", "Cannot add more than 4 players.");
        }
        players.put(player.getName(), player);
        log.info("Added player: {}", player);
        log.info("Current number of players: {}", players.size());
    }

    @Override
    public void startGame() {
        if (players.size() < 2) {
            throw new GameException("err-101", "Minimum number of players is 2 to start the game.");
        }
        if (players.size() > 4) {
            throw new GameException("err-102", "Maximum number of players is 4.");
        }

        this.gameStarted = true;
        this.winner = null;

        int winningScore = gameProperties.getWinningScore();

        log.info("Game started with {} players. Winning score is {}", players.size(), winningScore);

        while (!hasWinner()) {
            for (Player player : players.values()) {
                rollDiceForPlayer(player);
                if (hasWinner()) {
                    break;
                }
            }
        }
    }

    public boolean hasWinner() {
        return winner != null;
    }

    @Override
    public Player getWinner() {
        return winner;
    }

    /**
     * Rolls the dice for a given player according to game rules.
     *
     * @param player The player whose dice will be rolled.
     */
    private void rollDiceForPlayer(Player player) {
        int diceRoll = getDiceRollWithFallback();
        log.info("Player name: {} Total Score: {} Current Value of Dice: {}", player.getName(), player.getScore(), diceRoll);

        if (!player.isStarted()) {
            if (diceRoll == 6) {
                log.info("Player {} starts the game by rolling a 6", player.getName());
                player.setStarted(true);
                diceRoll = getDiceRollWithFallback();
                log.info("Player name: {} Total Score: {} Current Value of Dice: {}", player.getName(), player.getScore(), diceRoll);
                if (diceRoll == 6) {
                    log.info("Player {} rolled a 6 again after starting, score remains 0", player.getName());
                    player.setScore(0);
                } else {
                    player.setScore(diceRoll);
                    log.info("Player {}'s starting score is {}", player.getName(), player.getScore());
                }
            } else {
                log.info("Player {} must wait, did not roll a 6", player.getName());
                return;
            }
        } else {
            if (diceRoll == 4) {
                if (player.getScore() > 0) {
                    player.setScore(Math.max(0, player.getScore() - 4));
                    log.info("Player {} loses 4 points for rolling a 4. Total score: {}", player.getName(), player.getScore());
                } else {
                    log.info("Player {} rolled a 4, but score is already 0. No points deducted.", player.getName());
                }
            } else if (diceRoll == 6) {
                log.info("Player {} gets an extra roll for rolling a 6", player.getName());
                rollDiceForPlayer(player);
            } else {
                player.setScore(player.getScore() + diceRoll);
                log.info("Player {}'s total score is now {}", player.getName(), player.getScore());
            }

            if (player.getScore() >= gameProperties.getWinningScore()) {
                winner = player;
                log.info("Player {} has won the game with a score of {}", player.getName(), player.getScore());
            }
        }
    }

    @Override
    public List<Player> getCurrentScores() {
        return new ArrayList<>(players.values());
    }

    /**
     * Attempts to get a dice roll from the API. Falls back to a random roll if the API call fails.
     *
     * @return The dice roll result, either from the API or randomly generated.
     */
    private int getDiceRollWithFallback() {
        try {
            Integer diceRoll = restTemplate.getForObject(gameProperties.getInnerApiUrl(), Integer.class);
            if (diceRoll == null) {
                log.info("Dice roll API returned null, generating a random fallback value.");
                return diceRoller.roll();
            }
            return diceRoll;
        } catch (ResourceAccessException e) {
            log.info("Failed to call dice roll API, using random fallback due to connection error: {}", e.getMessage());
            return diceRoller.roll();
        } catch (Exception e) {
            log.info("An unexpected error occurred while calling dice roll API, using random fallback.", e);
            return diceRoller.roll();
        }
    }
}
