package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.response.AddContentResponse;
import com.film.blue_rabb.dto.response.ContentVideo;
import com.film.blue_rabb.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/video")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер для контента", description = "Контроллер позволяет работать с контентом киноискусства")
public class ContentController {
    private final ContentService contentService;

    @Operation(
            summary = "Метод создания информации о контенте киноискусства",
            description = "Позволяет создать и сохранить информацию о контенте киноискусства"
    )
    @PostMapping
    public ResponseEntity<AddContentResponse> postContent(
            @Parameter(description = "Информация о контенте киноискусства") @Valid @RequestPart AddContentRequest addContentRequest,
            @Parameter(description = "Изображения контента") @RequestPart("file") MultipartFile[] multipartFiles,
            @Parameter(description = "токен доступа") @RequestHeader("Authorization") String token
    ) {
            log.trace("ContentController.postContent - POST '/api/video' - contentVideo {}, token {}", addContentRequest, token);
            AddContentResponse addContentResponse = contentService.addContent(addContentRequest, multipartFiles, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(addContentResponse);
    }
}
