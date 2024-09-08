package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import lombok.Getter;

import java.util.List;

@Getter
//@RequiredArgsConstructor
public class ElementNotFoundException extends RuntimeException {
    private final List<ErrorMessage> errorMessage;

    public ElementNotFoundException(List<ErrorMessage> errorMessages) {
        super("ElementNotFound");
        this.errorMessage = errorMessages;
    }
}