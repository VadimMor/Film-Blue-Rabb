package com.film.blue_rabb.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum StatusEnum implements GrantedAuthority {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BANNEDTIME("BANNED_TIME"),
    BANNED("BANNED");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
