package org.mytodoapp.todo.security.auth.util;

import org.mytodoapp.todo.security.auth.dto.AuthRequestDto;
import org.mytodoapp.todo.security.auth.dto.AuthResponseDto;
import org.mytodoapp.todo.security.auth.dto.RefreshTokenRequestDto;
import org.mytodoapp.todo.security.auth.dto.RegisterRequestDto;

public class AuthTestDataFactory {

    public static RegisterRequestDto createRegisterReqDto(String username, String email, String password, String confirmPassword) {
        return RegisterRequestDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }

    public static AuthRequestDto createAuthReqDto(String email, String password) {
        return AuthRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static AuthResponseDto createAuthRespDto(String accessToken,
                                                    String refreshToken,
                                                    Long expiresIn,
                                                    String email,
                                                    String role) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .email(email)
                .role(role)
                .build();
    }

    public static RefreshTokenRequestDto createRefreshTokenReqDto(String refreshToken) {
        return RefreshTokenRequestDto.builder()
                .refreshToken(refreshToken)
                .build();
    }

}
