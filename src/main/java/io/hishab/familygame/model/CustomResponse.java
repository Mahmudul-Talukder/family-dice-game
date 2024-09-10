package io.hishab.familygame.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomResponse {
    private String result;
    private String message;
}