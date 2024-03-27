package com.ducquyet.websocket.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtExpiration extends RuntimeException{
    private String message;
    public JwtExpiration(String message) {
        super(message);
    }
}
