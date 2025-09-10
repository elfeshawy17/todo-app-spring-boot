package org.mytodoapp.todo.user.service;

import org.mytodoapp.todo.user.dto.UserCreateDto;
import org.mytodoapp.todo.user.dto.UserResponseDto;
import org.mytodoapp.todo.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserResponseDto add(UserCreateDto userCreateDto);
    UserResponseDto update(Long id, UserUpdateDto userUpdateDto);
    void delete(Long id);
    UserResponseDto findById(Long id);
    List<UserResponseDto> findAll();

}
