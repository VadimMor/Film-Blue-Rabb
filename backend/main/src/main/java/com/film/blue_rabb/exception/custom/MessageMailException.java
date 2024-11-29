package com.film.blue_rabb.exception.custom;

import lombok.Getter;

@Getter
public class MessageMailException extends RuntimeException {
    public MessageMailException() {
        super("Error sending a message to the user. Please try again");
    }
}
