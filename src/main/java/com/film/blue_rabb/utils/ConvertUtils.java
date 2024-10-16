package com.film.blue_rabb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.utils.validator.AddContentRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConvertUtils {
    private final AddContentRequestValidator addContentRequestValidator;

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

    public AddContentRequest convertToJSONContentRequest(String stringObject) throws JsonProcessingException, MethodArgumentNotValidException {
        log.trace("ConvertUtils.convertToJSONEventCreate - stringObject {}", stringObject);
        ObjectMapper objectMapper = new ObjectMapper();

        AddContentRequest request = objectMapper.readValue(stringObject, AddContentRequest.class);
        addContentRequestValidator.validate(request);
        return request;
    }
}
