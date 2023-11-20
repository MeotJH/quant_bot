package com.server.quant_bot.comm.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getter and setter methods
}
