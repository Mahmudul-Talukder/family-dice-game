package io.bjit.familygame.service;

import io.bjit.familygame.exception.GameException;
import io.bjit.familygame.model.Player;

import java.util.List;

public interface GameService {

    void addPlayer(Player player) throws GameException;

    void startGame() throws GameException;

    List<Player> getCurrentScores();

    Player getWinner();
}
