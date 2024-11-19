package com.film.blue_rabb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoResponse(
    @JsonProperty("full_name")
    String fullName,

    @JsonProperty("short_name")
    String shortName,

    @JsonProperty("description")
    String description,

    @JsonProperty("duration_minutes")
    Integer durationMinutes
) {
}
