package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomJsonParseException extends JsonProcessingException {
    private final List<ErrorMessage> errorMessage;

    public CustomJsonParseException(List<ErrorMessage> errorMessage) {
        super(errorMessage.get(0).message());
        this.errorMessage = errorMessage;
    }
}
