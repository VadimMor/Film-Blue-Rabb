package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.response.AddContentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ContentService {
    /**
     * Метод добавления контента киноиндустрии
     * @param addContentRequest информацияи о контенте
     * @param files изображения контента
     * @return сообщение об успешном создании контента
     */
    public AddContentResponse addContent(AddContentRequest addContentRequest, MultipartFile[] files) throws IOException;

    /**
     * Метод обновления информации контента киноискусства
     * @param addContentRequest информацияи о контенте
     * @param multipartFiles изображения контента
     * @param symbolicName символичное имя контента
     * @return сообщение об успешном обновлении контента
     */
    AddContentResponse updateContent(AddContentRequest addContentRequest, MultipartFile[] multipartFiles, String symbolicName) throws Exception;
}
