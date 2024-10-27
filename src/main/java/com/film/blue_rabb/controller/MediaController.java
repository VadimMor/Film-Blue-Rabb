package com.film.blue_rabb.controller;

import com.film.blue_rabb.dto.response.PublicImage;
import com.film.blue_rabb.service.ContentImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/media")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Контроллер для медиа контента", description = "Контроллер позволяет работать с медиа ресурсами")
public class MediaController {
    private final ContentImgService contentImgService;

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
}
