package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class ValueOutOfRangeException extends RuntimeException {
    private final List<ErrorMessage> errorMessage;

    public ValueOutOfRangeException(List<ErrorMessage> errorMessages) {
        super("ValueOutOfRangeException");
        this.errorMessage = errorMessages;
    }
}
