package com.film.blue_rabb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record AuthForm(
        @Pattern(regexp = "(\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})|((?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,28})", message = "Invalid email/login format")
        @NotBlank(message = "Email is mandatory!")
        String login,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$", message = "Invalid password email")
        @NotBlank(message = "Password is mandatory!")
        String password
) {
}
