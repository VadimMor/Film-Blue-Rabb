package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.request.AuthForm;
import com.film.blue_rabb.dto.request.RegistrationUserRequest;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.dto.response.RegistrationUserResponse;
import com.film.blue_rabb.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
@Tag(name = "Контроллер безопасности", description = "Всё, что связано с безопасностью")
public class SecurityController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет пройти аутентификацию и получить jwt"
    )
    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> createAuthToken(@Parameter(description = "Форма авторизации") @Valid @RequestBody AuthForm authForm) {
        log.trace("SecurityController.createAuthToken /api/security/auth - authForm {}", authForm);
        // Аутентификация переданный объект
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.login(), authForm.password()));
        // Аутентификация и проверка пользователя
        AuthResponse authResponse = userService.getAuthToken(authForm);
        // Возвращение JSON web token
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегестрировать пользователя и сохранить в бд"
    )
    @PostMapping("/reg")
    public ResponseEntity<RegistrationUserResponse> createUser(@Parameter(description = "Информация для регистрации") @Valid @RequestBody RegistrationUserRequest registrationUserRequest) throws MessagingException {
        log.trace("SecurityController.createUser /api/security/reg - registrationUserRequest {}", registrationUserRequest);
        RegistrationUserResponse response = userService.createUser(registrationUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
