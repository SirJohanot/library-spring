package com.patiun.libraryspring.configuration;

import com.patiun.libraryspring.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final String DEFAULT_EXCEPTION_VIEW = "error";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public final String serviceException(ServiceException serviceException) {
        LOGGER.warn("A service exception occurred while processing a request", serviceException);
        return DEFAULT_EXCEPTION_VIEW;
    }
}