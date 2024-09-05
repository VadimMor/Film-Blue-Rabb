package com.film.blue_rabb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddContentRequest(
        @JsonProperty("full_name")
        String fullName,

        @JsonProperty("description")
        String description,

        @JsonProperty("duration_minutes")
        Integer durationMinutes
) {
}
