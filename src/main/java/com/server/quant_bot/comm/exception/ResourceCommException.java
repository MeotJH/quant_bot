package com.server.quant_bot.comm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceCommException extends RuntimeException{

    private HttpStatus httpStatus;

    public ResourceCommException(String message) {
        super(message);
    }

    public ResourceCommException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
