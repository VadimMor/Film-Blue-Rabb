package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.request.AuthForm;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@Tag(name = "Контроллер безопасности", description = "Всё, что связано с безопасностью")
public class SecurityController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Ползволяется пройти аутентификацию и получить jwt"
    )
    @Hidden
    @PostMapping("/auth")//
    public ResponseEntity<AuthResponse> createAuthToken(
            @Parameter(description = "Форма авторизации") @Valid @RequestBody AuthForm authForm,
            @Parameter(description = "Контейнер сервлета") HttpServletRequest httpRequest
    ) {
        log.trace("SecurityController.createAuthToken /auth - authForm {}", authForm);
        // Аутентификация переданный объект
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.email(), authForm.password()));
        // Аутентификация и проверка пользователя
        AuthResponse authResponse = userService.getAuthToken(authForm, httpRequest);
        // Возвращение JSON web token
        return ResponseEntity.ok(authResponse);
    }
}
