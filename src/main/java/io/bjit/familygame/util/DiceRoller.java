package io.bjit.familygame.util;

import java.util.Random;

public class DiceRoller {

    private final Random random = new Random();

    public int roll() {
        return random.nextInt(6) + 1; // Returns a value between 1 and 6
    }
}
