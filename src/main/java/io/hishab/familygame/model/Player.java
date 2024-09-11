package io.hishab.familygame.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {

    private String name;
    private Integer age;
    private boolean started = false;
    private int score;
}

