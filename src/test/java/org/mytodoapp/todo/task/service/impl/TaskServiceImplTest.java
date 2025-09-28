package org.mytodoapp.todo.task.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mytodoapp.todo.shared.exception.RecordNotFoundException;
import org.mytodoapp.todo.task.dto.TaskRequestDto;
import org.mytodoapp.todo.task.dto.TaskResponseDto;
import org.mytodoapp.todo.task.entity.Task;
import org.mytodoapp.todo.task.mapper.TaskMapper;
import org.mytodoapp.todo.task.repo.TaskRepo;
import org.mytodoapp.todo.task.util.TaskTestDataFactory;
import org.mytodoapp.todo.user.entity.Role;
import org.mytodoapp.todo.user.entity.User;
import org.mytodoapp.todo.user.repo.UserRepo;
import org.mytodoapp.todo.user.util.UserTestDataFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepo taskRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User user;
    private Task task;
    private TaskRequestDto requestDto;
    private TaskResponseDto responseDto;

    @BeforeEach
    void setup() {
        user = UserTestDataFactory.createUser(1L,
                "testUser",
                "testEmail",
                "tsetPass",
                Role.USER);

        task = TaskTestDataFactory.createTask(1L,
                "testTitle",
                "testDesc",
                user);

        requestDto = TaskTestDataFactory.createTaskRequestDto("testTitle", "testDesc");

        responseDto = TaskTestDataFactory.createTaskResponseDto(1L,
                "testTitle",
                "testDesc",
                1L);
    }

    @Test
    void givenExistingUser_whenAddTask_thenSucceed() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(taskMapper.toResponseDto(task)).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.add(user.getId(), requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getUserId(), result.getUserId());
        assertEquals(responseDto.getTitle(), result.getTitle());
        assertEquals(responseDto.getDescription(), result.getDescription());

        verify(userRepo).findById(user.getId());
        verify(taskMapper).toEntity(requestDto);
        verify(taskRepo).save(task);
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    void givenNonExistingUser_whenAddTask_thenThrowRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> taskService.add(user.getId(), requestDto));

        // Assert
        verify(userRepo).findById(user.getId());
        verifyNoInteractions(taskMapper);
        verifyNoInteractions(taskRepo);
    }

    @Test
    void givenValidTaskAndUser_whenUpdateTask_thenSucceed() {
        // Arrange
        requestDto.setTitle("updated testTitle");
        requestDto.setDescription("updated testDesc");

        responseDto.setTitle(requestDto.getTitle());
        responseDto.setDescription(requestDto.getDescription());

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDto(any(Task.class))).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.update(user.getId(), task.getId(), requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getUserId(), result.getUserId());
        assertEquals(responseDto.getTitle(), result.getTitle());
        assertEquals(responseDto.getDescription(), result.getDescription());

        verify(userRepo).findById(user.getId());
        verify(taskRepo).findByIdAndUserId(task.getId(), user.getId());
        verify(taskRepo).save(task);
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    void givenValidTaskAndInvalidUser_whenUpdateTask_thenThrowRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> taskService.update(user.getId(), task.getId(), requestDto));

        // Assert
        verify(userRepo).findById(user.getId());
        verifyNoInteractions(taskRepo);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void givenInvalidTaskAndValidUser_whenUpdateTask_thenThrowRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> taskService.update(user.getId(), task.getId(), requestDto));

        // Assert
        verify(userRepo).findById(user.getId());
        verify(taskRepo).findByIdAndUserId(task.getId(), user.getId());
        verifyNoInteractions(taskMapper);
    }

    @Test
    void givenValidTaskAndUser_whenDeleteTask_thenSucceed() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.of(task));

        // Act
        taskService.delete(user.getId(), task.getId());

        // Assert
        verify(userRepo).findById(user.getId());
        verify(taskRepo).findByIdAndUserId(task.getId(), user.getId());
        verify(taskRepo).delete(task);
    }

    @Test
    void givenValidTaskAndInvalidUser_whenDeleteTask_thenThrowRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> taskService.delete(user.getId(), task.getId()));

        // Assert
        verify(userRepo).findById(user.getId());
        verifyNoInteractions(taskRepo);
    }

    @Test
    void givenInvalidTaskAndValidUser_whenDeleteTask_thenThrowRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> taskService.delete(user.getId(), task.getId()));

        // Assert
        verify(userRepo).findById(user.getId());
        verify(taskRepo).findByIdAndUserId(task.getId(), user.getId());
    }

    @Test
    void givenValidUserAndTask_whenFindTaskByUserId_thenReturnDto() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDto(task)).thenReturn(responseDto);

        // Act
        TaskResponseDto result = taskService.findTaskByUserId(user.getId(), task.getId());

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getTitle(), result.getTitle());
        assertEquals(responseDto.getDescription(), result.getDescription());

        verify(userRepo).findById(user.getId());
        verify(taskRepo).findByIdAndUserId(task.getId(), user.getId());
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    void givenInvalidUser_whenFindTaskByUserId_thenThrowRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class,
                () -> taskService.findTaskByUserId(user.getId(), task.getId()));

        verify(userRepo).findById(user.getId());
        verifyNoInteractions(taskRepo, taskMapper);
    }

    @Test
    void givenValidUserAndInvalidTask_whenFindTaskByUserId_thenThrowRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepo.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class,
                () -> taskService.findTaskByUserId(user.getId(), task.getId()));

        verify(userRepo).findById(user.getId());
        verify(taskRepo).findByIdAndUserId(task.getId(), user.getId());
        verifyNoInteractions(taskMapper);
    }

    @Test
    void givenValidUser_whenFindAllByUserId_thenSucceed() {
        // Arrange
        when(taskRepo.findTasksByUserId(user.getId())).thenReturn(List.of(task));
        when(taskMapper.toResponseDto(task)).thenReturn(responseDto);

        // Act
        List<TaskResponseDto> result = taskService.findAllByUserId(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDto.getTitle(), result.getFirst().getTitle());

        verify(taskRepo).findTasksByUserId(user.getId());
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    void givenNoTasks_whenFindAllByUserId_thenReturnEmptyList() {
        // Arrange
        when(taskRepo.findTasksByUserId(user.getId())).thenReturn(List.of());

        // Act
        List<TaskResponseDto> result = taskService.findAllByUserId(user.getId());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(taskRepo).findTasksByUserId(user.getId());
        verifyNoInteractions(taskMapper);
    }

}
