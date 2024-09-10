package io.hishab.familygame.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    private int statusCode;
    private String errorCode;
    private String message;
}

