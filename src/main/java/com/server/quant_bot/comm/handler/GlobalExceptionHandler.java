package com.server.quant_bot.comm.handler;

import com.server.quant_bot.comm.dto.ErrorResponse;
import com.server.quant_bot.comm.exception.ResourceCommException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceCommException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceCommException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        if(httpStatus == null){
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        ErrorResponse response = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return new ResponseEntity<>(response, httpStatus);
    }


}
