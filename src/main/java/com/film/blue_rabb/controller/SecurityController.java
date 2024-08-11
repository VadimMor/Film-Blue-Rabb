package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.request.LoginClientRequest;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер безопасности", description = "Контроллер позволяет работать с методами, связанными с безопасностью")
public class SecurityController {
    private final UserService userService;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет пройти аутентификацию пользователя и получить токен авторизации"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> postLogin(@RequestBody @Valid LoginClientRequest login) {
        log.info("SecurityController.postLogin /api/security/login - login {}", login);
        AuthResponse auth = userService.postAuthClient(login);
        return ResponseEntity.ok(auth);
    }
}
