package com.film.blue_rabb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.response.AddContentResponse;
import com.film.blue_rabb.dto.response.ContentVideo;
import com.film.blue_rabb.service.ContentImgService;
import com.film.blue_rabb.service.ContentService;
import com.film.blue_rabb.utils.ConvertUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/video")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер для контента", description = "Контроллер позволяет работать с контентом киноискусства")
public class ContentController {
    private final ContentService contentService;

    private final ConvertUtils convertToJSON;

    @Operation(
            summary = "Метод создания информации о контенте киноискусства",
            description = "Позволяет создать и сохранить информацию о контенте киноискусства"
    )
    @PostMapping
    public ResponseEntity<AddContentResponse> postContent(
            @Parameter(description = "Информация о контенте киноискусства")
            @Valid @RequestPart(name = "add_content_request") String addContentRequestJson,
            @Parameter(description = "Изображения контента")
            @RequestPart("file") MultipartFile[] multipartFiles
    ) throws IOException, MethodArgumentNotValidException {
        log.trace("ContentController.postContent - POST '/api/video' - contentVideo {}, count files {}", addContentRequestJson, multipartFiles.length);

        AddContentRequest addContentRequest = convertToJSON.convertToJSONContentRequest(addContentRequestJson);

        AddContentResponse addContentResponse = contentService.addContent(addContentRequest, multipartFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(addContentResponse);
    }

    @Operation(
            summary = "Метод изменения информации о контенте киноискусства",
            description = "Позволяет изменить и сохранить информацию о контенте киноискусства в базе данных"
    )
    @PutMapping
    public ResponseEntity<AddContentResponse> putContent(
            @Parameter(description = "Информация о контенте киноискусства")
            @Valid @RequestPart(name = "add_content_request") String addContentRequestJson,
            @Parameter(description = "Изображения контента")
            @RequestPart("file") MultipartFile[] multipartFiles,
            @RequestParam("symbolic-name") String symbolicName
    ) throws Exception {
        log.trace("ContentController.putContent - PUT '/api/video' - contentVideo {}, count files {}, symbolicName {}", addContentRequestJson, multipartFiles.length, symbolicName);

        AddContentRequest addContentRequest = convertToJSON.convertToJSONContentRequest(addContentRequestJson);

        AddContentResponse addContentResponse = contentService.updateContent(addContentRequest, multipartFiles, symbolicName);
        return ResponseEntity.status(HttpStatus.CREATED).body(addContentResponse);
    }
}
