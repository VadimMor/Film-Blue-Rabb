package com.film.blue_rabb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ContentVideo(
        @JsonProperty("name_rus")
        String nameRus,

        @JsonProperty("name_eng")
        String nameEng,

        @JsonProperty("description")
        String description,

        @JsonProperty("main_image")
        String mainImage,

        @JsonProperty("images")
        String[] images,

        @JsonProperty("favourite")
        Boolean favourite,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("age")
        Integer age,

        @JsonProperty("actors")
        String[] actors,

        @JsonProperty("creator")
        String creator
) {
}
