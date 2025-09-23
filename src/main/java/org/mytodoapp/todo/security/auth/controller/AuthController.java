package org.mytodoapp.todo.security.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mytodoapp.todo.security.auth.dto.AuthRequestDto;
import org.mytodoapp.todo.security.auth.dto.AuthResponseDto;
import org.mytodoapp.todo.security.auth.dto.RefreshTokenRequestDto;
import org.mytodoapp.todo.security.auth.dto.RegisterRequestDto;
import org.mytodoapp.todo.security.auth.service.AuthService;
import org.mytodoapp.todo.shared.dto.ApiResponse;
import org.mytodoapp.todo.shared.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ResponseBuilder responseBuilder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register (@Valid @RequestBody RegisterRequestDto dto) {
        AuthResponseDto response = authService.register(dto);
        return responseBuilder.created(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login (@Valid @RequestBody AuthRequestDto dto) {
        AuthResponseDto response = authService.login(dto);
        return responseBuilder.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refresh(@RequestBody RefreshTokenRequestDto dto) {
        AuthResponseDto response = authService.refreshToken(dto.getRefreshToken());
        return responseBuilder.ok(response);
    }

}
