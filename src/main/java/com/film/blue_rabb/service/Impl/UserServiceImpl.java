package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.utils.JwtTokenUtils;
import com.film.blue_rabb.dto.request.AuthForm;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.model.Users;
import com.film.blue_rabb.exception.ErrorMessage;
import com.film.blue_rabb.exception.custom.InvalidDataException;
import com.film.blue_rabb.repository.UserRepository;
import com.film.blue_rabb.service.*;
import com.film.blue_rabb.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @return токен авторизации
     */
    @Override
    @Transactional
    public AuthResponse getAuthToken(AuthForm authForm) {
        log.trace("UserServiceImpl.getAuthToken - authForm {}", authForm);
        Users user = userRepository.findFirstByEmail(authForm.email());

        // Проверка на правильно введнные данные и не актвиный аккаунт
        if (user == null
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email or password is incorrect!"));
            throw new InvalidDataException(errorMessages);
        }

        // Детали пользователя для токена
        UserDetails userDetails = new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );

        return new AuthResponse(
                jwtTokenUtils.generateToken(userDetails)
        );
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, EntityNotFoundException {
        log.trace("UserServiceImpl.loadUserByUsername - email {}", email);

        Users user = userRepository.findFirstByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("Entity with email " + email + " not found");
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );
    }
}
