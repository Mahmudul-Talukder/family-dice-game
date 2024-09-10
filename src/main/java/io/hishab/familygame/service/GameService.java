package io.hishab.familygame.service;

import io.hishab.familygame.exception.GameException;
import io.hishab.familygame.model.Player;

import java.util.List;

public interface GameService {

    void addPlayer(Player player) throws GameException;

    void startGame() throws GameException;

    List<Player> getCurrentScores();

    Player getWinner();
}
