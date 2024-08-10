package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.response.ContentVideo;
import com.film.blue_rabb.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/video")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер контента", description = "Контроллер для работы с контентом")
public class VideoController {
    private final VideoService videoService;

    @Operation(
            summary = "Вывод контента",
            description = "Вывод полной информации о контенте"
    )
    @GetMapping
    public ResponseEntity<ContentVideo> getVideoContent(@RequestParam(value = "name", defaultValue = "0-none") String name) {
        log.trace("VideoController.getVideoContent /video - name {}", name);
        ContentVideo contentVideo = videoService.getContent(name);
        return ResponseEntity.ok(contentVideo);
    };
}
