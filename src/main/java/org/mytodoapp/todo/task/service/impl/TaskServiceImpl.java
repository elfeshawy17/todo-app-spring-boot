package org.mytodoapp.todo.task.service.impl;

import lombok.AllArgsConstructor;
import org.mytodoapp.todo.shared.exception.RecordNotFoundException;
import org.mytodoapp.todo.task.dto.TaskRequestDto;
import org.mytodoapp.todo.task.dto.TaskResponseDto;
import org.mytodoapp.todo.task.entity.Task;
import org.mytodoapp.todo.task.mapper.TaskMapper;
import org.mytodoapp.todo.task.repo.TaskRepo;
import org.mytodoapp.todo.task.service.TaskService;
import org.mytodoapp.todo.user.entity.User;
import org.mytodoapp.todo.user.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponseDto add(Long userId, TaskRequestDto dto) {
        User user = findUserByIdOrThrow(userId);
        Task task = taskMapper.toEntity(dto);
        task.setUser(user);
        taskRepo.save(task);
        return taskMapper.toResponseDto(task);
    }

    @Override
    public TaskResponseDto update(Long userId, Long taskId, TaskRequestDto dto) {
        findUserByIdOrThrow(userId);
        Task existingTask = taskRepo.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RecordNotFoundException("Task", taskId));

        taskMapper.updateTaskFromDto(dto, existingTask);
        taskRepo.save(existingTask);

        return taskMapper.toResponseDto(existingTask);
    }

    @Override
    public void delete(Long userId, Long taskId) {
        findUserByIdOrThrow(userId);
        Task task = taskRepo.findByIdAndUserId(taskId, userId).orElseThrow(() -> new RecordNotFoundException("Task", taskId));
        taskRepo.delete(task);
    }

    @Override
    public TaskResponseDto findTaskByUserId(Long userId, Long taskId) {
        findUserByIdOrThrow(userId);
        Task task = taskRepo.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RecordNotFoundException("Task", taskId));
        return taskMapper.toResponseDto(task);
    }

    @Override
    public List<TaskResponseDto> findAllByUserId(Long userId) {
        return taskRepo.findTasksByUserId(userId)
                .stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User", userId));
    }

}
