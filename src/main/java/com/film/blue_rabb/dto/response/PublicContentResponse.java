package com.film.blue_rabb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PublicContentResponse(
        @JsonProperty("name_rus")
        String nameRus,

        @JsonProperty("name_eng")
        String nameEng,

        @JsonProperty("description")
        String description,

        @JsonProperty("symbolic-name")
        String symbolicName,

        @JsonProperty("main_image")
        String mainImage,

        @JsonProperty("favourite")
        Boolean favourite,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("age")
        Integer age
) {
}
