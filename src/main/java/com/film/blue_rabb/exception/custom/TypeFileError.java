package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class TypeFileError extends RuntimeException {
    private final List<ErrorMessage> errorMessage;

    public TypeFileError(List<ErrorMessage> errorMessages) {
        super("Invalid file type");
        this.errorMessage = errorMessages;
    }
}
