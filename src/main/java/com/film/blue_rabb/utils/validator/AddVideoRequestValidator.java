package com.film.blue_rabb.utils.validator;

import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.request.AddVideoRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

@Component
public class AddVideoRequestValidator {
    public void validate(AddVideoRequest request) throws MethodArgumentNotValidException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "AddVideoRequest");

        validateNotBlank(request.fullName(), "fullName", "Full name is mandatory!", bindingResult);
        validateNotBlank(request.description(), "description", "Description is mandatory!", bindingResult);

        if (request.durationMinutes() == null) {
            bindingResult.rejectValue("durationMinutes", "durationMinutes.null", "Duration minutes is mandatory!");
        }

        if (bindingResult.hasErrors()) {
            throw createMethodArgumentNotValidException(bindingResult);
        }
    }

    private MethodArgumentNotValidException createMethodArgumentNotValidException(BindingResult bindingResult) {
        Method method;
        try {
            method = this.getClass().getMethod("validate", AddContentRequest.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found", e);
        }
        MethodParameter parameter = new MethodParameter(method, 0);
        return new MethodArgumentNotValidException(parameter, bindingResult);
    }

    private void validateNotBlank(String value, String field, String message, BindingResult bindingResult) {
        if (value == null || (value instanceof String && ((String) value).isBlank())) {
            bindingResult.rejectValue(field, field + ".empty", message);
        }
    }
}
