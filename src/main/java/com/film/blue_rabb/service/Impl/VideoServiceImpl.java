package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.response.ContentVideo;
import com.film.blue_rabb.repository.VideoRepository;
import com.film.blue_rabb.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;

    /**
     * Метод получения информации о видео
     * @param name нзвание и id
     * @return информация о видео
     */
    @Override
    public ContentVideo getContent(String name) {
        log.info("VideoServiceImpl.ContentVideo - name {}", name);



        return null;
    }
}
