package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.request.LoginClientRequest;
import com.film.blue_rabb.dto.request.RegistrationUserRequest;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.dto.response.RegistrationUserResponse;
import com.film.blue_rabb.exception.custom.AuthenticationUserException;
import com.film.blue_rabb.exception.custom.BannedException;
import com.film.blue_rabb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер безопасности", description = "Контроллер позволяет работать с методами, связанными с безопасностью")
public class SecurityController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Async
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет пройти аутентификацию и получить jwt"
    )
    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> createAuthToken(@Parameter(description = "Форма авторизации") @Valid @RequestBody LoginClientRequest authForm) throws AuthenticationUserException, BannedException {
        log.trace("SecurityController.createAuthToken - POST /api/security/auth - authForm {}", authForm);
        // Аутентификация переданный объект
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.login(), authForm.password()));
        // Аутентификация и проверка пользователя
        AuthResponse authResponse = userService.postAuthClient(authForm);
        // Возвращение JSON web token
        return ResponseEntity.ok(authResponse);
    }

    @Async
    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегестрировать аккаунт и сохранить в бд"
    )
    @PostMapping("/reg")
    public ResponseEntity<RegistrationUserResponse> createUser(@Parameter(description = "Информация для регистрации") @Valid @RequestBody RegistrationUserRequest registrationUserRequest) throws MessagingException, MessagingException {
        log.trace("SecurityController.createUser - POST /api/security/reg - registrationUserRequest {}", registrationUserRequest);
        RegistrationUserResponse response = userService.createUser(registrationUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Активация аккаунта",
            description = "Позволяет активировать аккаунт для дальнейшей работы"
    )
    @PutMapping("/reg")
    public ResponseEntity<RegistrationUserResponse> activeUser(
            @Parameter(description = "код для активации") @RequestParam String code,
            @Parameter(description = "почта для поиска аккаунта") @RequestParam String email
    ) {
        log.trace("SecurityController.activeUser - PUT /api/security/reg - code {}, email {}", code, email);
        RegistrationUserResponse response = userService.activeUser(code, email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
