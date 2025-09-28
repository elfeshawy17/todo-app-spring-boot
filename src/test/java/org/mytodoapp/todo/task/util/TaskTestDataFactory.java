package org.mytodoapp.todo.task.util;

import org.mytodoapp.todo.task.dto.TaskRequestDto;
import org.mytodoapp.todo.task.dto.TaskResponseDto;
import org.mytodoapp.todo.task.entity.Task;
import org.mytodoapp.todo.user.entity.User;

public class TaskTestDataFactory {

    public static Task createTask(Long id, String title, String desc, User user) {
        return Task.builder()
                .id(id)
                .title(title)
                .description(desc)
                .user(user)
                .build();
    }

    public static TaskRequestDto createTaskRequestDto(String title, String desc) {
        return TaskRequestDto.builder()
                .title(title)
                .description(desc)
                .build();
    }

    public static TaskResponseDto createTaskResponseDto(Long id, String title, String desc, Long userId) {
        return TaskResponseDto.builder()
                .id(id)
                .title(title)
                .description(desc)
                .userId(userId)
                .build();
    }

}
