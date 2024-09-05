package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.response.AddContentResponse;
import com.film.blue_rabb.repository.ContentRepository;
import com.film.blue_rabb.service.ContentService;
import com.film.blue_rabb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final UserService userService;

    private final ContentRepository contentRepository;

    /**
     * Метод добавления контента киноиндустрии
     * @param addContentRequest информацияи о контенте
     * @param files изображения контента
     * @param token токен авторизации
     * @return сообщение об успешном создании контента
     */
    @Override
    public AddContentResponse addContent(AddContentRequest addContentRequest, MultipartFile[] files, String token) {
        log.trace("ContentServiceImpl.addContent - addContentRequest {}, count files {}, token {}", addContentRequest, files.length, token);

        userService.checkToken(token);

        return null;
    }
}
