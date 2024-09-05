package com.film.blue_rabb.service;

import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.response.AddContentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ContentService {
    /**
     * Метод добавления контента киноиндустрии
     * @param addContentRequest информацияи о контенте
     * @param files изображения контента
     * @param token токен авторизации
     * @return сообщение об успешном создании контента
     */
    public AddContentResponse addContent(AddContentRequest addContentRequest, MultipartFile[] files, String token);
}
