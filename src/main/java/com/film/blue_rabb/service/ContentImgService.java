package com.film.blue_rabb.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ContentImgService {
    /**
     * Сохранение изображений контента
     * @param file изображение
     */
    String saveContentImg(MultipartFile file) throws IOException;

    void deleteSavedImages(List<String> savedImages);
}
