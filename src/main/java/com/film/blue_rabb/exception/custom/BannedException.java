package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
public class BannedException extends RuntimeException {
    private final boolean ip;
    private final OffsetDateTime banEndTime;

    public BannedException(boolean ip, OffsetDateTime banEndTime) {
        super(createMessage(ip, banEndTime));
        this.ip = ip;
        this.banEndTime = banEndTime;
    }

    public BannedException(boolean ip) {
        super(createMessage(ip, null));
        this.ip = ip;
        this.banEndTime = null;
    }

    public BannedException() {
        super(createMessage(false, null));
        this.ip = false;
        this.banEndTime = null;
    }

    private static String createMessage(boolean profile, OffsetDateTime banEndTime) {
        return !profile
                ? "Access is denied to the resource is closed"
                : banEndTime == null
                    ? "The user is banned"
                    : String.format("The user is banned until %s", banEndTime);
    }
}
