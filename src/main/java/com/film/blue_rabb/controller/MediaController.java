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
            @RequestParam Long id,
            HttpServletRequest request
    ) {
        log.trace("MediaController.getImage - GET /api/media/video, id {}", id);

        // Получение объекта VideoFile из сервиса по идентификатору
        Optional<VideoFile> videoFileOpt = videoService.getVideoFile(id);

        // Если файл не найден, логируем предупреждение и возвращаем статус 404 (NOT_FOUND)
        if (videoFileOpt.isEmpty()) {
            log.warn("Video file not found for id {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Извлечение объекта VideoFile
        VideoFile videoFile = videoFileOpt.get();

        // Получение данных видео, типа контента и размера файла
        byte[] videoData = videoFile.getVideo();
        String contentType = videoFile.getContentType();
        long fileSize = videoFile.getSize();

        // Проверка на наличие данных видео (не должно быть null или пустым)
        if (videoData == null || videoData.length == 0) {
            log.warn("Video data is null or empty for id {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Чтение заголовка Range из запроса (если он есть)
        String rangeHeader = request.getHeader(HttpHeaders.RANGE);
        long rangeStart = 0; // Начальная позиция по умолчанию
        long rangeEnd = fileSize - 1; // Конечная позиция по умолчанию (весь файл)

        // Если заголовок Range присутствует, разбираем его для определения диапазона
        if (rangeHeader != null) {
            String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
            try {
                rangeStart = Long.parseLong(ranges[0]); // Начальная позиция
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    rangeEnd = Long.parseLong(ranges[1]); // Конечная позиция (если указана)
                }
            } catch (NumberFormatException e) {
                // Логируем ошибку формата диапазона и возвращаем статус 416 (REQUESTED_RANGE_NOT_SATISFIABLE)
                log.error("Invalid range format: {}", rangeHeader);
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(null);
            }
        }

        // Финальные значения диапазона и длина контента
        final long finalRangeStart = rangeStart;
        final long finalRangeEnd = rangeEnd;
        final long contentLength = finalRangeEnd - finalRangeStart + 1;

        // Если заголовок Range есть, устанавливаем статус PARTIAL_CONTENT, иначе OK
        HttpStatus status = rangeHeader != null ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;

        // Создаем поток для передачи данных видео клиенту
        StreamingResponseBody responseBody = outputStream -> {
            try {
                // Пишем часть данных в соответствии с диапазоном
                outputStream.write(videoData, (int) finalRangeStart, (int) contentLength);
            } catch (IOException e) {
                // Логируем ошибку в процессе передачи видео
                log.error("Error streaming video for id {}", id, e);
            }
        };

        // Возвращаем ответ с установленными заголовками и телом для потоковой передачи
        return ResponseEntity.status(status)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + finalRangeStart + "-" + finalRangeEnd + "/" + fileSize)
                .body(responseBody);

    }

}
