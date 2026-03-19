package com.tp1cicd;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleStudentNotFound(final StudentNotFoundException exception) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEmailAlreadyUsed(final EmailAlreadyUsedException exception) {
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(final Exception exception) {
        return Map.of("error", resolveMessage(exception));
    }

    private String resolveMessage(final Exception exception) {
        if (exception instanceof MethodArgumentNotValidException validationException) {
            return validationException.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .orElse("Requete invalide");
        }
        if (exception instanceof MethodArgumentTypeMismatchException mismatchException) {
            return "L'id doit etre un nombre valide";
        }
        if (exception instanceof HttpMessageNotReadableException messageNotReadableException) {
            return "Le corps de la requete est invalide";
        }
        if (exception instanceof MissingServletRequestParameterException missingParameterException) {
            return "Le parametre " + missingParameterException.getParameterName() + " est requis";
        }
        return exception.getMessage();
    }
}
