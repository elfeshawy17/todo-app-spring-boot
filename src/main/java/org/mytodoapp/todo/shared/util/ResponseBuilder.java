package org.mytodoapp.todo.shared.util;

import org.mytodoapp.todo.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResponseBuilder {

    public <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(data));
    }

    public <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    public ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    public <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, List<String> errors) {
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status, message, errors));
    }
    
}
