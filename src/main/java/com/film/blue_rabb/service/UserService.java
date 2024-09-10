package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.request.AuthForm;
import com.film.blue_rabb.dto.request.RegistrationUserRequest;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.dto.response.RegistrationUserResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @return токен авторизации
     */
    AuthResponse getAuthToken(AuthForm authForm);

    /**
     * Метод создания пользователя и сохранение в бд
     * @param registrationUserRequest данные для регистрации
     * @return данные о успешной регистрации
     */
    RegistrationUserResponse createUser(RegistrationUserRequest registrationUserRequest) throws MessagingException;

    /**
     * Метод активации аккаунта
     * @param code код активации
     * @param email почта для поиска аккаунта
     * @return данные о успешной регистрации
     */
    RegistrationUserResponse activeUser(String code, String email);
}
