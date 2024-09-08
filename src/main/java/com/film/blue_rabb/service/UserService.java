package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.request.AuthForm;
import com.film.blue_rabb.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @param httpRequest Контейнер сервлета
     * @return токен авторизации
     */
    AuthResponse getAuthToken(AuthForm authForm, HttpServletRequest httpRequest);

}
