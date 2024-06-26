package com.patiun.libraryspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
