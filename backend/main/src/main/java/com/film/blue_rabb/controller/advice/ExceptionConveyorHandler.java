package com.film.blue_rabb.controller.advice;

import org.springframework.security.core.AuthenticationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.film.blue_rabb.exception.ErrorMessage;
import com.film.blue_rabb.exception.ErrorResponse;
import com.film.blue_rabb.exception.custom.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionConveyorHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = getValidationErrorMessage(ex);
        ErrorResponse errorResponse =
                createResponseException(HttpStatus.BAD_REQUEST, String.valueOf(MethodArgumentNotValidException.class), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getValidationErrorMessage(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return String.join(". ", errors);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public ErrorResponse onInvalidFormatException(InvalidFormatException e) {
        return createResponseException(HttpStatus.BAD_REQUEST, e.getTargetType().toString(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorResponse onInvalidFormatException(EntityExistsException e) {
        return createResponseException(HttpStatus.BAD_REQUEST, "Entity already exists", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse onInvalidApplicationId(EntityNotFoundException ex) {
        return createResponseException(HttpStatus.NOT_FOUND, "Entity not found", ex.getMessage());
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Forbidden.class)
    public ErrorResponse onForbiddenException(Forbidden e) {
        return createResponseException(HttpStatus.FORBIDDEN, "Forbidden", e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ErrorResponse errorResponse = createResponseException(
                HttpStatus.UNAUTHORIZED,
                "JWT token has expired",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        String message = ex.getMessage();
        String path = "";

        if (ex instanceof FileNotFoundException) {
            FileNotFoundException fnfe = (FileNotFoundException) ex;
            path = fnfe.getMessage();
            // Извлекаем только имя файла из полного пути
            path = new File(path).getName();
        }

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "File Operation Error",
                message + (path.isEmpty() ? " non FNFE" : " File: " + path)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleSignatureException(SignatureException ex) {
        return createResponseException(
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT signature",
                "The JWT token's signature is not valid"
        );
    }

    @ExceptionHandler(TypeFileError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileTypeException(TypeFileError ex) {
        String errorMessage = ex.getErrorMessage().stream()
                .map(error -> error.fieldName() + ": " + error.message())
                .collect(Collectors.joining(", "));

        return createResponseException(
                HttpStatus.BAD_REQUEST,
                "File error",
                errorMessage
        );
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> handleJsonParseException(JsonParseException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Ошибка разбора JSON: " + ex.getMessage());
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<String> handleJsonMappingException(JsonMappingException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Ошибка сопоставления JSON: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Произошла ошибка: " + ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyCreatedException(UserAlreadyCreatedException e) {
        return createResponseException(
                HttpStatus.BAD_REQUEST,
                "Registration error",
                e.getMessage()
        );
    }
    @ResponseBody
    @ExceptionHandler(MessageMailException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleMessageMailException(MessageMailException e) {
        return createResponseException(
                HttpStatus.BAD_REQUEST,
                "Send email error",
                e.getMessage()
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public List<ErrorMessage> onInvalidDataException(InvalidDataException e) {
        return e.getInvalidFields();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse onAuthenticationException(AuthenticationException ex) {
        return createResponseException(HttpStatus.UNAUTHORIZED, "The email/login or password is incorrect!", ex.getMessage());
    }

    //new excep handlers
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ElementNotFoundException.class)
    public ErrorResponse onElementNotFoundException(ElementNotFoundException ex) {
        String errorMessage = ex.getErrorMessage().stream()
                .map(error -> error.fieldName() + ": " + error.message())
                .collect(Collectors.joining(", "));
        return createResponseException(HttpStatus.NOT_FOUND, "Element not found", errorMessage);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse onUserNotFoundException(UserNotFoundException ex) {
        String errorMessage = ex.getErrorMessage().stream()
                .map(error -> error.fieldName() + ": " + error.message())
                .collect(Collectors.joining(", "));
        return createResponseException(HttpStatus.NOT_FOUND, "User not found", errorMessage);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse onAccessDeniedException(AccessDeniedException ex) {
        String errorMessage = ex.getErrorMessage().stream()
                .map(error -> error.fieldName() + ": " + error.message())
                .collect(Collectors.joining(", "));
        return createResponseException(HttpStatus.UNAUTHORIZED, "Access denied", errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationUserException e) {
        String errorMessage = e.getErrorMessage().stream()
                .map(error -> error.message())
                .collect(Collectors.joining(", "));
        return createResponseException(HttpStatus.UNAUTHORIZED, "Authentication error", errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(BannedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBannedException(BannedException e) {
        return createResponseException(
                HttpStatus.BAD_REQUEST,
                "Access is denied",
                e.getMessage()
        );
    }


    private ErrorResponse createResponseException(HttpStatus status, String error, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message
        );
    }
}