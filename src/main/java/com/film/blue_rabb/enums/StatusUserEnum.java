package com.film.blue_rabb.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatusUserEnum {
    ACTIVE("ACTIVE"),
    BANNED("BANNED"),
    INACTIVE("INACTIVE");

    private final String value;

    public String getStatusUser() {
        return value;
    }
}
