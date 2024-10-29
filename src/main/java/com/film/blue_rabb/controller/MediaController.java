package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.response.PublicImage;
import com.film.blue_rabb.model.VideoFile;
import com.film.blue_rabb.service.ContentImgService;
import com.film.blue_rabb.service.VideoService;
import io.github.classgraph.Resource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/media")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер для медиа контента", description = "Контроллер позволяет работать с медиа ресурсами")
public class MediaController {
    private final ContentImgService contentImgService;
    private final VideoService videoService;

    @Operation(
            summary = "Метод вывода изображения",
            description = "Позволяет вывести изображение из бд"
    )
    @GetMapping
    public void getImage(
            @RequestParam("name") String name,
            @RequestParam("w") Integer width,
            @RequestParam("h") Integer height,
            HttpServletResponse response
    ) throws IOException {
        log.trace("MediaController.getImage - GET /api/media, name {}, width {}, height {}", name, width, height);
        PublicImage publicImage = contentImgService.getImage(name);

        response.setContentType(publicImage.type());

        try {
            // Преобразуем byte[] в BufferedImage
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(publicImage.imageData()));
            // Создаем пустое изображение с новыми размерами
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // Масштабируем оригинальное изображение до новых размеров
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();
            // Записываем масштабированное изображение в выходной поток
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, publicImage.type().replace("image/", ""), outputStream);
            // Отправляем измененное изображение в ответ
            response.getOutputStream().write(outputStream.toByteArray());
        } catch (IOException e) {
            log.error("IOException occurred while processing the image: {}", e.getMessage());
            throw new IOException("Failed to process image due to an IO error", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("Unexpected error while processing image", e);  // Генерация общего исключения
        } finally {
            response.getOutputStream().close();
        }
    }

    @Operation(
            summary = "Метод стриминга видео",
            description = "Позволяет транслировать видео из бд"
    )
    @GetMapping("/video")
    public ResponseEntity<StreamingResponseBody> getVideos(
            @Parameter(description = "id информации видео")
            @RequestParam Long id,
            HttpServletRequest request
    ) {
        log.trace("MediaController.getImage - GET /api/media/video, id {}", id);
        Optional<VideoFile> videoFile = videoService.getVideoFile(id);

        long fileSize = videoFile.get().getVideo().length;
        String contentType = videoFile.get().getContentType();

        // Получаем range-запрос (если есть)
        String rangeHeader = request.getHeader(HttpHeaders.RANGE);
        long rangeStart = 0;
        long rangeEnd = fileSize - 1;

        if (rangeHeader != null) {
            // Обработка range-запроса
            String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
            rangeStart = Long.parseLong(ranges[0]);
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            }
        }

        // Устанавливаем длину передаваемого контента
        final long contentLength = rangeEnd - rangeStart + 1;
        HttpStatus status = rangeHeader != null ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;

        final long finalRangeStart = rangeStart;
        final long finalContentLength = contentLength;

        return ResponseEntity.status(status)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", rangeStart, rangeEnd, fileSize))
                .body(outputStream -> outputStream.write(videoFile.get().getVideo(), (int) finalRangeStart, (int) finalContentLength));
    }
}
