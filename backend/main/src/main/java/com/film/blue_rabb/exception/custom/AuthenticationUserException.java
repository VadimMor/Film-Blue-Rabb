package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class AuthenticationUserException extends RuntimeException {
    private final List<ErrorMessage> errorMessage;

    public AuthenticationUserException(List<ErrorMessage> errorMessages) {
        super("Authentication error");
        this.errorMessage = errorMessages;
    }
}
