package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class EmailSendingException extends RuntimeException {
    private final List<ErrorMessage> errorMessage;

    public EmailSendingException(List<ErrorMessage> errorMessages) {
        super("Server Email error");
        this.errorMessage = errorMessages;
    }
}