package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.request.LoginClientRequest;
import com.film.blue_rabb.dto.request.RegistrationUserRequest;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.dto.response.RegistrationUserResponse;
import com.film.blue_rabb.model.Users;
import jakarta.mail.MessagingException;
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

    /**
     * Получение пользователя по токену
     * @param token токен авторизации
     * @return возвращает авторизированного пользователя
     */
    Users getUserByToken(String token);
}
