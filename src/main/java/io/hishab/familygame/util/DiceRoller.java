package io.hishab.familygame.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DiceRoller {

    private final Random random = new Random();

    /**
     * Rolls the dice and returns a value between 1 and 6.
     *
     * @return the result of the dice roll
     */
    public int roll() {
        return random.nextInt(6) + 1; // Returns a value between 1 and 6
    }
}

