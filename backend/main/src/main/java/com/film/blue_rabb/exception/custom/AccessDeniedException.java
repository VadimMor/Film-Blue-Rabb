package com.film.blue_rabb.exception.custom;

import com.film.blue_rabb.exception.ErrorMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import lombok.Getter;

import java.util.List;

@Getter
public class AccessDeniedException extends ExpiredJwtException {
    private final List<ErrorMessage> errorMessage;

    public AccessDeniedException(Header header, Claims claims, List<ErrorMessage> errorMessages) {
        super(header, claims, "Access Denied: JWT expired");
        this.errorMessage = errorMessages;
    }
}
