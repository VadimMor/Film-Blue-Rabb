package com.film.blue_rabb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.exception.custom.InvalidDataException;
import com.film.blue_rabb.utils.validator.AddContentRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConvertUtils {
    private final AddContentRequestValidator addContentRequestValidator;

    @Value("${app.secret-key-name}")
    private String KEY_NAME;

    private static final String ALGORITHM = "DESede";

    /**
     * Форматирует строку, переводя её в нижний регистр, заменяя пробелы на дефисы и удаляя все неалфавитные символы.
     * @param input строка, которую нужно отформатировать
     * @return отформатированная строка
     */
    public String formatName(String input) {
        // Приводим к нижнему регистру
        String formatted = input.toLowerCase();

        // Заменяем пробелы на дефисы и удаляем все неалфавитные символы
        formatted = formatted.replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");

        return formatted;
    }

    /**
     * Форматирует строку в JSON объект для контента
     * @param stringObject строка для преобразования
     * @return возвращает преобразованный JSON объект
     * @throws JsonProcessingException ошибка преобразования строки в JSON
     * @throws MethodArgumentNotValidException ошибка, что аргумент отсутствует
     */
    public AddContentRequest convertToJSONContentRequest(String stringObject) throws JsonProcessingException, MethodArgumentNotValidException {
        log.trace("ConvertUtils.convertToJSONEventCreate - stringObject {}", stringObject);
        ObjectMapper objectMapper = new ObjectMapper();

        AddContentRequest request = objectMapper.readValue(stringObject, AddContentRequest.class);
        addContentRequestValidator.validate(request);

        return request;
    }

    /**
     * Метод для расшифровки строки, зашифрованной с использованием Triple DES
     * @param data зашифрованная строка в формате Base64
     * @return расшифрованная строка
     * @throws Exception если возникает ошибка расшифровки
     */
    public String decryptIdContent(String data) throws Exception {
        // Преобразуем ключ в 24 байта
        byte[] keyBytes = new byte[24];
        byte[] originalKeyBytes = KEY_NAME.getBytes();
        System.arraycopy(originalKeyBytes, 0, keyBytes, 0, Math.min(originalKeyBytes.length, 24));

        // Создаем SecretKeySpec с заданным ключом и алгоритмом Triple DES
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // Инициализируем шифр в режиме расшифровки
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // Расшифровываем данные из формата Base64 и возвращаем исходную строку
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decrypted);
    }

    /**
     * Метод для шифрования строки с использованием Triple DES
     * @param data строка, которую нужно зашифровать
     * @return зашифрованная строка в формате Base64
     * @throws Exception если возникает ошибка шифрования
     */
    public String encryptIdContent(String data) throws Exception {
        // Преобразуем ключ в 24 байта (если длина ключа меньше 24 байт, заполняем оставшиеся нулями)
        byte[] keyBytes = new byte[24];
        byte[] originalKeyBytes = KEY_NAME.getBytes();
        System.arraycopy(originalKeyBytes, 0, keyBytes, 0, Math.min(originalKeyBytes.length, 24));

        // Создаем SecretKeySpec с заданным ключом и алгоритмом Triple DES
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // Инициализируем шифр в режиме шифрования
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Шифруем данные и кодируем результат в строку Base64
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
