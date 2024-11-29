package com.film.blue_rabb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassiveContentResponse(
        @JsonProperty("content_responses")
        PublicContentResponse[] contentResponses
) {
}
