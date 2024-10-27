package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.response.PublicImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public interface ContentImgService {
    /**
     * Сохранение изображений контента
     * @param file изображение
     */
    String saveContentImg(MultipartFile file) throws IOException;

    boolean checkContentImg(MultipartFile file, Set<String> idImgs) throws IOException;

    /**
     * Метод удаления изображений, сохраненных в бд
     * @param savedImages список id сохраненных изображений
     */
    void deleteSavedImages(List<String> savedImages);

    /**
     * Метод получения данных для вывода изображения
     * @param name id изображения
     * @return данные для вывода
     */
    PublicImage getImage(String name);
}
