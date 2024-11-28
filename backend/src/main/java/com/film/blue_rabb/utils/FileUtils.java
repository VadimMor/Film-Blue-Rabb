package com.film.blue_rabb.utils;

import com.film.blue_rabb.exception.ErrorMessage;
import com.film.blue_rabb.exception.custom.TypeFileError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class FileUtils {
    /**
     * Проверяет, является ли файл допустимым изображением (JPEG или PNG) на основе его MIME-типа и сигнатуры.
     * @param bytes байты файла для проверки сигнатуры
     * @param contentType MIME-тип файла
     * @throws TypeFileError если файл не является допустимым изображением или имеет неверный формат
     */
    public static void checkTypeImage(byte[] bytes, String contentType) throws TypeFileError {
        log.trace("FileUtils.checkTypeImage - Checking file type with contentType: {}", contentType);

        // Проверяем тип содержимого
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            log.warn("Invalid content type detected: {}", contentType);
            throw createTypeFileError("The file is not an image");
        }

        // Проверка сигнатуры
        if (bytes.length < 8) {
            log.warn("File is too small to be a valid image. Size: {} bytes", bytes.length);
            throw createTypeFileError("The file is too small");
        }

        // JPEG (FF D8 FF)
        if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 && bytes[2] == (byte) 0xFF) {
            log.trace("File identified as JPEG image.");
            return; // JPEG
        }

        // PNG (89 50 4E 47 0D 0A 1A 0A)
        if (bytes[0] == (byte) 0x89 && bytes[1] == (byte) 0x50 && bytes[2] == (byte) 0x4E &&
                bytes[3] == (byte) 0x47 && bytes[4] == (byte) 0x0D && bytes[5] == (byte) 0x0A &&
                bytes[6] == (byte) 0x1A && bytes[7] == (byte) 0x0A) {
            log.trace("File identified as PNG image.");
            return; // PNG
        }

        log.warn("File signature does not match known image types.");
        throw createTypeFileError("The file is not an image");
    }

    /**
     * Проверяет, является ли файл допустимым видеофайлом (MP4 или AVI) на основе его MIME-типа и сигнатуры.
     * @param bytes байты файла для проверки сигнатуры
     * @param contentType MIME-тип файла
     * @throws TypeFileError если файл не является допустимым изображением или имеет неверный формат
     */
    public static void checkTypeVideo(byte[] bytes, String contentType) throws TypeFileError {
        log.trace("FileUtils.checkTypeVideo - Checking file type with contentType: {}", contentType);

        // Проверяем MIME-тип
        if (contentType == null || (!contentType.equals("video/mp4") && !contentType.equals("video/x-msvideo"))) {
            log.warn("Invalid content type detected: {}", contentType);
            throw createTypeFileError("The file is not a valid video");
        }

        // Проверка длины файла (минимальная длина сигнатуры для MP4 и AVI)
        if (bytes.length < 12) {
            log.warn("File is too small to be a valid video. Size: {} bytes", bytes.length);
            throw createTypeFileError("The file is too small");
        }

        // MP4 (00 00 00 ?? 66 74 79 70 69 73 6F 6D)
        if (bytes[0] == (byte) 0x00 && bytes[1] == (byte) 0x00 && bytes[2] == (byte) 0x00 &&
                (bytes[3] >= 0x18 && bytes[3] <= 0x21) && // иногда 0x18 - 0x21
                bytes[4] == (byte) 0x66 && bytes[5] == (byte) 0x74 &&
                bytes[6] == (byte) 0x79 && bytes[7] == (byte) 0x70 &&
                bytes[8] == (byte) 0x69 && bytes[9] == (byte) 0x73 &&
                bytes[10] == (byte) 0x6F && bytes[11] == (byte) 0x6D) {
            log.trace("File identified as MP4 video.");
            return; // MP4
        }

        // AVI (52 49 46 46 .... 41 56 49 20)
        if (bytes[0] == (byte) 0x52 && bytes[1] == (byte) 0x49 &&
                bytes[2] == (byte) 0x46 && bytes[3] == (byte) 0x46 &&
                bytes[8] == (byte) 0x41 && bytes[9] == (byte) 0x56 &&
                bytes[10] == (byte) 0x49 && bytes[11] == (byte) 0x20) {
            log.trace("File identified as AVI video.");
            return; // AVI
        }

        log.warn("File signature does not match known video types.");
        throw createTypeFileError("The file is not a valid video");
    }

    /**
     * Создаёт экземпляр исключения TypeFileError с указанным сообщением об ошибке.
     * @param message текст сообщения об ошибке, описывающий причину возникновения ошибки типа файла.
     * @return объект TypeFileError, содержащий список сообщений об ошибках с переданным сообщением.
     */
    private static TypeFileError createTypeFileError(String message) {
        log.error("Creating TypeFileError with message: {}", message);

        ArrayList<ErrorMessage> errorMessages = new ArrayList<>();
        errorMessages.add(new ErrorMessage("File type error", message));

        return new TypeFileError(errorMessages);
    }
}
