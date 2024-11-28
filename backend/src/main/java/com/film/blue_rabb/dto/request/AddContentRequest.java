package com.film.blue_rabb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public record AddContentRequest(
        @JsonProperty("full_name")
        @NotBlank(message = "Полное имя не должно быть пустым")
        String fullName,

        @JsonProperty("full_name_rus")
        @NotBlank(message = "Полное имя на русском не должно быть пустым")
        String fullNameRus,

        @JsonProperty("description")
        @NotBlank(message = "Описание не должно быть пустым")
        String description,

        @JsonProperty("age")
        @NotNull(message = "Возраст не должен быть null")
        @Min(value = 0, message = "Возраст должен быть не меньше 0")
        byte age,

        @JsonProperty("creator")
        @NotBlank(message = "Создатель не должен быть пустым")
        String creator,

        @JsonProperty("durationMinutes")
        @NotNull(message = "Длительность в минутах не должна быть null")
        @Min(value = 0, message = "Длительность в минутах должна быть не меньше 0")
        Integer durationMinutes
) {
}
