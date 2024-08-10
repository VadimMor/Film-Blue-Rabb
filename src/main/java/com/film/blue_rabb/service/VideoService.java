package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.response.ContentVideo;
import org.springframework.stereotype.Service;

@Service
public interface VideoService {
    /**
     * Метод получения информации о видео
     * @param name нзвание и id
     * @return информация о видео
     */
    ContentVideo getContent(String name);
}
