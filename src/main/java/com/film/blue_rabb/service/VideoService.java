package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.request.AddVideoRequest;
import com.film.blue_rabb.dto.response.VideoResponse;
import com.film.blue_rabb.model.Video;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface VideoService {
    /**
     * Метод добавления видео и информации о нем в бд
     * @param file файл видео
     * @param addVideoRequest информация о видео
     * @return сохраненной видео
     */
    Video addVideo(MultipartFile file, AddVideoRequest addVideoRequest) throws IOException;

    /**
     * Метод получения информации о видео
     * @param id id видео
     * @return информация о видео
     */
    VideoResponse getVideo(Long id);
}
