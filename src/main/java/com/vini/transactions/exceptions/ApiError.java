package com.vini.transactions.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {
    private HttpStatus status;
    private String message;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
}