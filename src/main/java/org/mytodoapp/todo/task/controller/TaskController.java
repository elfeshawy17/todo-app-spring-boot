package org.mytodoapp.todo.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mytodoapp.todo.shared.dto.ApiResponse;
import org.mytodoapp.todo.shared.util.ResponseBuilder;
import org.mytodoapp.todo.task.dto.TaskRequestDto;
import org.mytodoapp.todo.task.dto.TaskResponseDto;
import org.mytodoapp.todo.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ResponseBuilder responseBuilder;

    @PostMapping()
    public ResponseEntity<ApiResponse<TaskResponseDto>> addTask(
            @PathVariable Long userId,
            @Valid @RequestBody TaskRequestDto taskRequestDto
    ) {
        TaskResponseDto createdTask = taskService.add(userId, taskRequestDto);
        return responseBuilder.created(createdTask);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getAllTasks(@PathVariable Long userId){
        List<TaskResponseDto> tasks = taskService.findAllByUserId(userId);
        return responseBuilder.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(@PathVariable Long userId, @PathVariable Long taskId){
        TaskResponseDto task = taskService.findTaskByUserId(userId, taskId);
        return responseBuilder.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequestDto dto
    ) {
        TaskResponseDto updatedTask = taskService.update(userId, taskId, dto);
        return responseBuilder.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long userId,
            @PathVariable Long taskId
    ) {
        taskService.delete(userId, taskId);
        return responseBuilder.noContent();
    }

}
