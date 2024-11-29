package com.film.blue_rabb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AddVideoRequest(
        @JsonProperty("full_name")
        @NotBlank(message = "Полное имя не должно быть пустым")
        String fullName,

        @JsonProperty("description")
        @NotBlank(message = "Описание не должно быть пустым")
        String description,

        @JsonProperty("duration_minutes")
        @NotNull(message = "Время просмотра не должно быть пустым")
        Integer durationMinutes
) {
}
