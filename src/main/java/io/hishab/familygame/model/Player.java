package io.hishab.familygame.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Age is mandatory")
    @Min(value = 1, message = "Age must be greater than 0")
    private Integer age;  // Use Integer instead of int for proper null checking

    private boolean started = false;

    private int score;

}
