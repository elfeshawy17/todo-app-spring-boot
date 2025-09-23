package org.mytodoapp.todo.shared.exception;

import lombok.RequiredArgsConstructor;
import org.mytodoapp.todo.shared.dto.ApiResponse;
import org.mytodoapp.todo.shared.util.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ResponseBuilder responseBuilder;

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleRecordNotFoundException(RecordNotFoundException ex) {
        return responseBuilder.error(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), List.of(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return responseBuilder.error(HttpStatus.BAD_REQUEST, "Validation Failed", errors);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return responseBuilder.error(HttpStatus.CONFLICT, ex.getLocalizedMessage(), List.of(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return responseBuilder.error(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), List.of(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        return responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), List.of(ex.getMessage()));
    }

}
