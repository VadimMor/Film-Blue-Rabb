package com.film.blue_rabb.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface VideoFileService {
    /**
     * Метод сохранения файла в бд
     * @param file файл видео
     * @return id файла
     */
    String saveVideoFile(MultipartFile file) throws IOException;

    /**
     * Удаление сохраненного видео файла
     * @param videoFile видео файл
     */
    void deleteVideoFile(String videoFile);
}
