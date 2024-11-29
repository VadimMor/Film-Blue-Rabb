package com.film.blue_rabb.exception.custom;

import lombok.Getter;

@Getter
public class UserAlreadyCreatedException extends RuntimeException {
    public UserAlreadyCreatedException() {
        super("User already exists");
    }
}
