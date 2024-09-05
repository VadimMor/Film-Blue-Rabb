package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.request.LoginClientRequest;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.model.User;
import com.film.blue_rabb.repository.UserRepository;
import com.film.blue_rabb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Метод аутентификации пользователя по почте или логину и паролю
     * @param loginRequest данные для аутентификации
     * @return токен авторизации
     */
    @Override
    public AuthResponse postAuthClient(LoginClientRequest loginRequest) {
        log.info("UserServiceImpl.postAuthClient - login {}", loginRequest);

        User user = userRepository.getFirstByEmailOrLogin(loginRequest.login());

        return null;
    }

    /**
     * Метод проверки токена
     * @param token токен авторизации
     */
    @Override
    public void checkToken(String token) {
       log.trace("UserServiceImpl.checkToken - token {}", token);


    }


}
