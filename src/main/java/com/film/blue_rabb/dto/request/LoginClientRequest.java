package com.film.blue_rabb.dto.request;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

@Validated
public record LoginClientRequest(
        @NotBlank(message = "Email or login is mandatory!")
        @Pattern(regexp = "(\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})|((?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,28})", message = "Invalid email or login format!")
        String login,

        @NotBlank(message = "Password is mandatory!")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,28}$", message = "The password must contain upper case, lower case, numbers, special characters (!, _, -, %, @, .)!")
        @Size(min = 8, max = 28, message = "The password must be at least 8 and no more than 28 characters long!")
        String password
) {
}
