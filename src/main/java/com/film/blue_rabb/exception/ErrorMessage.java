package com.film.blue_rabb.exception;

public record ErrorMessage(
        String fieldName,
        String message
) {
}
