package org.mytodoapp.todo.task.service;

import org.mytodoapp.todo.task.dto.TaskRequestDto;
import org.mytodoapp.todo.task.dto.TaskResponseDto;

import java.util.List;

public interface TaskService  {

    TaskResponseDto add(Long userId, TaskRequestDto dto);
    TaskResponseDto update(Long userId, Long taskId, TaskRequestDto dto);
    void delete(Long userId, Long taskId);
    TaskResponseDto findTaskByUserId(Long userId, Long taskId);
    List<TaskResponseDto> findAllByUserId(Long userId);

}
