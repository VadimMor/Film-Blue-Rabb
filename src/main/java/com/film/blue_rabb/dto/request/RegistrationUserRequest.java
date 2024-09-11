package com.film.blue_rabb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record RegistrationUserRequest(
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,28}$", message = "Invalid login format")
        @NotBlank(message = "Login is mandatory!")
        String login,

        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", message = "Invalid email format")
        @NotBlank
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$", message = "Invalid password format")
        @NotBlank(message = "Password is mandatory!")
        String password,

        @NotNull(message = "Birthday is mandatory!")
        LocalDate birthday
) {
}
