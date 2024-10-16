package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.request.LoginClientRequest;
import com.film.blue_rabb.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * Метод аутентификации пользователя по почте или логину и паролю
     * @param loginRequest данные для аутентификации
     * @return токен авторизации
     */
    public AuthResponse postAuthClient(LoginClientRequest loginRequest);

    /**
     * Метод проверки токена
     * @param token токен авторизации
     */
    void checkToken(String token);
}
