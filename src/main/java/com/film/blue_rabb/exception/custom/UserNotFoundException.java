package com.film.blue_rabb.exception.custom;


import com.film.blue_rabb.exception.ErrorMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final List<ErrorMessage> errorMessage;

    public UserNotFoundException(List<ErrorMessage> errorMessages) {
        super("User not found");
        this.errorMessage = errorMessages;
    }
}
