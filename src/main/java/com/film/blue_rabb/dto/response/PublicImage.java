package com.film.blue_rabb.dto.response;

public record PublicImage(
        byte[] imageData,
        String name,
        String type
) {
}
