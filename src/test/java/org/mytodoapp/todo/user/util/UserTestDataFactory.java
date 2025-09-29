package org.mytodoapp.todo.user.util;

import org.mytodoapp.todo.user.dto.UserCreateDto;
import org.mytodoapp.todo.user.dto.UserResponseDto;
import org.mytodoapp.todo.user.dto.UserUpdateDto;
import org.mytodoapp.todo.user.entity.Role;
import org.mytodoapp.todo.user.entity.User;

public class UserTestDataFactory {

    public static User createUser(Long id, String username, String email, String password, Role role) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    public static UserCreateDto createDto(String username, String email, String password) {
        return UserCreateDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    public static UserUpdateDto updateDto(Long id, String username, String email) {
        return UserUpdateDto.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }

    public static UserResponseDto responseDto(Long id, String username, String email) {
        return UserResponseDto.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }

}
