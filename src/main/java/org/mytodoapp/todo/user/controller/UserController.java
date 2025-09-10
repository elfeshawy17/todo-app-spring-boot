package org.mytodoapp.todo.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mytodoapp.todo.shared.dto.ApiResponse;
import org.mytodoapp.todo.shared.util.ResponseBuilder;
import org.mytodoapp.todo.user.dto.UserCreateDto;
import org.mytodoapp.todo.user.dto.UserResponseDto;
import org.mytodoapp.todo.user.dto.UserUpdateDto;
import org.mytodoapp.todo.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ResponseBuilder responseBuilder;

    @PostMapping()
    public ResponseEntity<ApiResponse<UserResponseDto>> addUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserResponseDto createdUser = userService.add(userCreateDto);
        return responseBuilder.created(createdUser);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.findAll();
        return responseBuilder.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long userId){
        UserResponseDto user = userService.findById(userId);
        return responseBuilder.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        UserResponseDto updatedUser = userService.update(userId, userUpdateDto);
        return responseBuilder.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return responseBuilder.noContent();
    }

}
