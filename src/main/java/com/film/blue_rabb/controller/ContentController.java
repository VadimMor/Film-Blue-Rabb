package com.film.blue_rabb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.request.AddVideoRequest;
import com.film.blue_rabb.dto.response.AddContentResponse;
import com.film.blue_rabb.dto.response.ContentResponse;
import com.film.blue_rabb.dto.response.VideoResponse;
import com.film.blue_rabb.service.ContentService;
import com.film.blue_rabb.service.VideoService;
import com.film.blue_rabb.utils.ConvertUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/video")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер для контента", description = "Контроллер позволяет работать с контентом киноискусства")
public class ContentController {
    private final ContentService contentService;
    private final VideoService videoService;

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

    @Operation(
            summary = "Метод получения информации о контенте киноискусства",
            description = "Позволяет посмотреть информацию о контенте киноискусства из бд"
    )
    @GetMapping
    public ResponseEntity<ContentResponse> getContent(
            @Parameter(description = "Символичное название киноискусства")
            @RequestParam("symbolic-name") String symbolicName,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        log.trace("ContentController.getContent - GET '/api/video' - symbolicName {}", symbolicName);
        ContentResponse contentResponse = contentService.getContent(symbolicName, token);
        return ResponseEntity.ok(contentResponse);
    }

    @Operation(
            summary = "Метод загрузки видео и информации о видео",
            description = "Позволяет загрузить видео файл и информацию о нем в бд"
    )
    @PostMapping("/media")
    public ResponseEntity<AddContentResponse> postVideo(
            @Parameter(description = "Файл видео")
            @RequestPart MultipartFile file,
            @Parameter(description = "Данные о видео")
            @RequestPart(value = "add_content_request_string") String addContentRequestString,
            @Parameter(description = "Символичное название киноискусства")
            @RequestParam("symbolic-name") String symbolicName
    ) throws IOException, MethodArgumentNotValidException {
        log.trace("ContentController.postVideo - POST '/api/video/media' - file {}, addContentRequestString {}, symbolicName {}", file.getOriginalFilename(), addContentRequestString, symbolicName);

        AddVideoRequest addVideoRequest = convertToJSON.convertToJSONVideoRequest(addContentRequestString);

        AddContentResponse addContentResponse = contentService.addVideo(file, addVideoRequest, symbolicName);
        return ResponseEntity.status(HttpStatus.CREATED).body(addContentResponse);
    }

    @Operation(
            summary = "Метод получения информации о видео",
            description = "Позволяет получить информацию о видео из бд"
    )
    @GetMapping("/media")
    public ResponseEntity<VideoResponse> getVideo(
            @Parameter(description = "Id видео")
            @RequestParam("name") Long id
    ) {
        log.trace("ContentController.getVideo - GET '/api/video/media' - id {}", id);
        VideoResponse videoResponse = videoService.getVideo(id);
        return ResponseEntity.ok(null);
    }
}
