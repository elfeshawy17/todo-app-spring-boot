package org.mytodoapp.todo.security.auth.service;

import org.mytodoapp.todo.security.auth.dto.AuthRequestDto;
import org.mytodoapp.todo.security.auth.dto.AuthResponseDto;
import org.mytodoapp.todo.security.auth.dto.RegisterRequestDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto dto);
    AuthResponseDto login(AuthRequestDto dto);
    AuthResponseDto refreshToken(String refreshToken);
}
