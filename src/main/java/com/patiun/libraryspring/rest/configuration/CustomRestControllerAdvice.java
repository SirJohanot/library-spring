package com.patiun.libraryspring.rest.configuration;

import com.patiun.libraryspring.exception.ServiceException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false",
        matchIfMissing = true)
public class CustomRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException cause) {
        BindingResult causeResult = cause.getBindingResult();
        if (causeResult.hasErrors()) {
            List<ObjectError> allErrors = causeResult.getAllErrors();
            ObjectError firstError = allErrors.get(0);
            String firstErrorMessage = firstError.getDefaultMessage();

            return Map.of("error", firstErrorMessage != null ? firstErrorMessage : "Bad request");
        }
        return Map.of("error", "Bad request");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public Map<String, String> handleServiceException(ServiceException cause) {
        String message = cause.getMessage();
        return Map.of("error", message);
    }

}
