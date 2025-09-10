package org.mytodoapp.todo.shared.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Integer status;
    private String message;
    private T data;
    private List<String> errors;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    // Factories
    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data, List<String> errors) {
        return new ApiResponse<>(status.value(), message, data, errors, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, "Ok", data, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return of(HttpStatus.CREATED, "Created", data, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, List<String> errors) {
        return of(status, message, null, errors);
    }

}
