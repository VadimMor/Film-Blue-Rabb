package com.film.blue_rabb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddContentResponse(
        @JsonProperty("name_content")
        String nameContent,

        @JsonProperty("message")
        String message
) {
}
