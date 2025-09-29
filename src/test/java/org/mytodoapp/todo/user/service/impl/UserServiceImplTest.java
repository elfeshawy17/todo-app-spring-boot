package org.mytodoapp.todo.user.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mytodoapp.todo.shared.exception.DuplicateResourceException;
import org.mytodoapp.todo.shared.exception.RecordNotFoundException;
import org.mytodoapp.todo.user.dto.UserCreateDto;
import org.mytodoapp.todo.user.dto.UserResponseDto;
import org.mytodoapp.todo.user.dto.UserUpdateDto;
import org.mytodoapp.todo.user.entity.Role;
import org.mytodoapp.todo.user.entity.User;
import org.mytodoapp.todo.user.mapper.UserMapper;
import org.mytodoapp.todo.user.repo.UserRepo;
import org.mytodoapp.todo.user.util.UserTestDataFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserCreateDto createDto;
    private UserUpdateDto updateDto;
    private UserResponseDto responseDto;

    @BeforeEach
    void setup() {
        user = UserTestDataFactory.createUser(1L,
                "test username",
                "test email",
                "encoded password",
                Role.USER);

        createDto = UserTestDataFactory.createDto("test username",
                "test email",
                "test password");

        updateDto = UserTestDataFactory.updateDto(1L,
                "updated test username",
                "updated test email");

        responseDto = UserTestDataFactory.responseDto(1L,
                "test username",
                "test email");
    }

    @Test
    void givenValidData_whenAddUser_thenSuccess() {
        // Arrange
        when(userRepo.existsByEmail(createDto.getEmail())).thenReturn(false);
        when(userRepo.findByUsername(createDto.getUsername())).thenReturn(Optional.empty());
        when(userMapper.toEntity(createDto)).thenReturn(user);
        when(passwordEncoder.encode(createDto.getPassword())).thenReturn("encoded password");
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.add(createDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        assertEquals(responseDto.getUsername(), result.getUsername());
        assertEquals(responseDto.getEmail(), result.getEmail());

        verify(userRepo).existsByEmail(user.getEmail());
        verify(userRepo).findByUsername(user.getUsername());
        verify(userMapper).toEntity(createDto);
        verify(passwordEncoder).encode(createDto.getPassword());
        verify(userRepo).save(user);
        verify(userMapper).toResponseDto(user);
    }

    @Test
    void givenInvalidEmail_whenAddUser_thenThrowsDuplicateResourceException() {
        // Arrange
        when(userRepo.existsByEmail(createDto.getEmail())).thenReturn(true);

        // Act
        assertThrows(DuplicateResourceException.class,
                () -> userService.add(createDto));

        // Assert
        verify(userRepo).existsByEmail(user.getEmail());
        verifyNoInteractions(userMapper, passwordEncoder);
    }

    @Test
    void givenInvalidUsername_whenAddUser_thenThrowsDuplicateResourceException() {
        // Arrange
        when(userRepo.findByUsername(createDto.getUsername())).thenReturn(Optional.of(user));

        // Act
        assertThrows(DuplicateResourceException.class,
                () -> userService.add(createDto));

        // Assert
        verify(userRepo).existsByEmail(user.getEmail());
        verify(userRepo).findByUsername(user.getUsername());
        verifyNoInteractions(userMapper, passwordEncoder);
    }

    @Test
    void givenValidUser_whenUpdateUser_thenSuccess() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        user.setUsername(updateDto.getUsername());
        user.setEmail(updateDto.getEmail());
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.update(user.getId(), updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        assertEquals(responseDto.getUsername(), result.getUsername());
        assertEquals(responseDto.getEmail(), result.getEmail());

        verify(userRepo).findById(user.getId());
        verify(userRepo).save(user);
        verify(userMapper).toResponseDto(user);
    }

    @Test
    void givenInvalidUser_whenUpdateUser_thenThrowsRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> userService.update(user.getId(), updateDto));

        // Assert
        verify(userRepo).findById(user.getId());
        verifyNoInteractions(userMapper);
    }

    @Test
    void givenValidUser_whenDeleteUser_thenSuccess() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        userService.delete(user.getId());

        // Assert
        verify(userRepo).findById(user.getId());
        verify(userRepo).deleteById(user.getId());
    }

    @Test
    void givenInvalidUser_whenDeleteUser_thenThrowsRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> userService.delete(user.getId()));

        // Assert
        verify(userRepo).findById(user.getId());
    }

    @Test
    void givenValidUser_whenFindById_thenSuccess() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.findById(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        assertEquals(responseDto.getUsername(), result.getUsername());
        assertEquals(responseDto.getEmail(), result.getEmail());

        verify(userRepo).findById(user.getId());
        verify(userMapper).toResponseDto(user);
    }

    @Test
    void givenInvalidUser_whenFindById_thenThrowsRecordNotFoundException() {
        // Arrange
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        // Act
        assertThrows(RecordNotFoundException.class,
                () -> userService.findById(user.getId()));

        // Assert
        verify(userRepo).findById(user.getId());
        verifyNoInteractions(userMapper);
    }

    @Test
    void whenFindAll_thenSucceed() {
        // Arrange
        when(userRepo.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        // Act
        List<UserResponseDto> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getFirst().getId());
        assertEquals(responseDto.getUsername(), result.getFirst().getUsername());
        assertEquals(responseDto.getEmail(), result.getFirst().getEmail());

        verify(userRepo).findAll();
        verify(userMapper).toResponseDto(user);
    }

    @Test
    void whenFindAll_thenReturnEmptyList() {
        // Arrange
        when(userRepo.findAll()).thenReturn(List.of());

        // Act
        List<UserResponseDto> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepo).findAll();
        verifyNoInteractions(userMapper);
    }
}
