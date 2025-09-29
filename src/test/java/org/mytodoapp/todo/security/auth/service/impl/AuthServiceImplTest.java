package org.mytodoapp.todo.security.auth.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mytodoapp.todo.security.auth.dto.AuthRequestDto;
import org.mytodoapp.todo.security.auth.dto.AuthResponseDto;
import org.mytodoapp.todo.security.auth.dto.RefreshTokenRequestDto;
import org.mytodoapp.todo.security.auth.dto.RegisterRequestDto;
import org.mytodoapp.todo.security.auth.mapper.AuthMapper;
import org.mytodoapp.todo.security.auth.util.AuthTestDataFactory;
import org.mytodoapp.todo.security.util.JwtUtil;
import org.mytodoapp.todo.shared.exception.DuplicateResourceException;
import org.mytodoapp.todo.user.entity.Role;
import org.mytodoapp.todo.user.entity.User;
import org.mytodoapp.todo.user.repo.UserRepo;
import org.mytodoapp.todo.user.util.UserTestDataFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private AuthMapper authMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private RegisterRequestDto registerRequestDto;
    private AuthRequestDto authRequestDto;
    private AuthResponseDto authResponseDto;
    private RefreshTokenRequestDto refreshTokenRequestDto;

    @BeforeEach
    public void setUp() {
        user = UserTestDataFactory.createUser(1L,
                "test username",
                "test email",
                "encoded password",
                Role.USER);

        registerRequestDto = AuthTestDataFactory.createRegisterReqDto(
                "test username",
                "test email",
                "test password",
                "test password"
        );

        authRequestDto = AuthTestDataFactory.createAuthReqDto(
                "test email",
                "test password"
        );

        authResponseDto = AuthTestDataFactory.createAuthRespDto(
                "access token",
                "refresh token",
                3600000L,
                "test email",
                "USER"
        );

        refreshTokenRequestDto = AuthTestDataFactory.createRefreshTokenReqDto(
                "refresh token"
        );
    }

    @Test
    void givenValidData_whenRegister_thenSucceed() {
        // Arrange
        when(userRepo.existsByEmail(registerRequestDto.getEmail())).thenReturn(false);
        when(authMapper.toUser(registerRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded password");
        when(jwtUtil.generateAccessToken(anyString(), anyMap())).thenReturn("access token");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refresh token");
        when(jwtUtil.getAccessExpirationMillis()).thenReturn(3600000L);

        // Act
        AuthResponseDto result = authService.register(registerRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(authResponseDto.getAccessToken(), result.getAccessToken());
        assertEquals(authResponseDto.getRefreshToken(), result.getRefreshToken());
        assertEquals(authResponseDto.getExpiresIn(), result.getExpiresIn());
        assertEquals(authResponseDto.getEmail(), result.getEmail());
        assertEquals(authResponseDto.getRole(), result.getRole());

        verify(userRepo).existsByEmail(user.getEmail());
        verify(authMapper).toUser(registerRequestDto);
        verify(userRepo).save(user);
        verify(jwtUtil).generateAccessToken(anyString(), anyMap());
        verify(jwtUtil).generateRefreshToken(anyString());
    }

    @Test
    void givenInvalidMail_whenRegister_thenThrowDuplicateResourceException() {
        // Arrange
        when(userRepo.existsByEmail(registerRequestDto.getEmail())).thenReturn(true);

        // Act
        assertThrows(DuplicateResourceException.class,
                () -> authService.register(registerRequestDto));

        // Assert
        verify(userRepo).existsByEmail(registerRequestDto.getEmail());
        verifyNoMoreInteractions(userRepo);
        verifyNoInteractions(authMapper, passwordEncoder, jwtUtil);
    }

    @Test
    void givenPasswordsDoNotMatch_whenRegister_thenThrowIllegalArgumentException() {
        // Arrange
        when(userRepo.existsByEmail(registerRequestDto.getEmail())).thenReturn(false);
        registerRequestDto.setConfirmPassword("different password");

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(registerRequestDto));

        // Assert
        verify(userRepo).existsByEmail(registerRequestDto.getEmail());
        verifyNoMoreInteractions(userRepo);
        verifyNoInteractions(authMapper, passwordEncoder, jwtUtil);
    }

    @Test
    void givenValidCredentials_whenLogin_thenSucceed() {
        // Arrange
        when(userRepo.findByEmail(registerRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken(anyString(), anyMap())).thenReturn("access token");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refresh token");
        when(jwtUtil.getAccessExpirationMillis()).thenReturn(3600000L);

        // Act
        AuthResponseDto result = authService.login(authRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(authResponseDto.getAccessToken(), result.getAccessToken());
        assertEquals(authResponseDto.getRefreshToken(), result.getRefreshToken());
        assertEquals(authResponseDto.getExpiresIn(), result.getExpiresIn());
        assertEquals(authResponseDto.getEmail(), result.getEmail());
        assertEquals(authResponseDto.getRole(), result.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepo).findByEmail(registerRequestDto.getEmail());
        verify(jwtUtil).generateAccessToken(anyString(), anyMap());
        verify(jwtUtil).generateRefreshToken(anyString());
    }

    @Test
    void givenInvalidEmail_whenLogin_thenUsernameNotFoundException() {
        // Arrange
        when(userRepo.findByEmail(registerRequestDto.getEmail())).thenReturn(Optional.empty());

        // Act
        assertThrows(UsernameNotFoundException.class,
                () -> authService.login(authRequestDto));

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepo).findByEmail(authRequestDto.getEmail());
        verifyNoMoreInteractions(userRepo);
        verify(jwtUtil, never()).generateAccessToken(anyString(), anyMap());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }

    @Test
    void givenInvalidPassword_whenLogin_thenThrowAuthenticationException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act
        assertThrows(BadCredentialsException.class,
                () -> authService.login(authRequestDto));

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepo, never()).findByEmail(anyString());
        verify(jwtUtil, never()).generateAccessToken(anyString(), anyMap());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }

    @Test
    void givenValidRefreshToken_whenRefreshToken_thenSucceed() {
        // Arrange
        when(jwtUtil.extractEmail(refreshTokenRequestDto.getRefreshToken())).thenReturn(user.getEmail());
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.isTokenValid(refreshTokenRequestDto.getRefreshToken(), user.getEmail())).thenReturn(true);
        when(jwtUtil.generateAccessToken(anyString(), anyMap())).thenReturn("access token");
        when(jwtUtil.getAccessExpirationMillis()).thenReturn(3600000L);

        // Act
        AuthResponseDto result = authService.refreshToken(refreshTokenRequestDto.getRefreshToken());

        // Assert
        assertNotNull(result);
        assertEquals(authResponseDto.getAccessToken(), result.getAccessToken());
        assertEquals(authResponseDto.getRefreshToken(), result.getRefreshToken());
        assertEquals(authResponseDto.getExpiresIn(), result.getExpiresIn());
        assertEquals(authResponseDto.getEmail(), result.getEmail());
        assertEquals(authResponseDto.getRole(), result.getRole());

        verify(jwtUtil).extractEmail(refreshTokenRequestDto.getRefreshToken());
        verify(userRepo).findByEmail(user.getEmail());
        verify(jwtUtil).isTokenValid(refreshTokenRequestDto.getRefreshToken(), user.getEmail());
        verify(jwtUtil).generateAccessToken(anyString(), anyMap());
        verify(jwtUtil).getAccessExpirationMillis();
    }

    @Test
    void givenInvalidEmail_whenRefreshToken_thenThrowUsernameNotFoundException() {
        // Arrange
        when(jwtUtil.extractEmail(refreshTokenRequestDto.getRefreshToken())).thenReturn(user.getEmail());
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act
        assertThrows(UsernameNotFoundException.class,
                () -> authService.refreshToken(refreshTokenRequestDto.getRefreshToken()));

        // Assert
        verify(jwtUtil).extractEmail(refreshTokenRequestDto.getRefreshToken());
        verify(userRepo).findByEmail(user.getEmail());
        verify(jwtUtil, never()).isTokenValid(anyString(), anyString());
        verify(jwtUtil, never()).generateAccessToken(anyString(), anyMap());
        verify(jwtUtil, never()).getAccessExpirationMillis();
    }

    @Test
    void givenInvalidRefreshToken_whenRefreshToken_thenThrowIllegalArgumentException() {
        // Arrange
        when(jwtUtil.extractEmail(refreshTokenRequestDto.getRefreshToken())).thenReturn(user.getEmail());
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.isTokenValid(refreshTokenRequestDto.getRefreshToken(), user.getEmail())).thenReturn(false);

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> authService.refreshToken(refreshTokenRequestDto.getRefreshToken()));

        // Assert
        verify(jwtUtil).extractEmail(refreshTokenRequestDto.getRefreshToken());
        verify(userRepo).findByEmail(user.getEmail());
        verify(jwtUtil).isTokenValid(refreshTokenRequestDto.getRefreshToken(), user.getEmail());
        verify(jwtUtil, never()).generateAccessToken(anyString(), anyMap());
        verify(jwtUtil, never()).getAccessExpirationMillis();
    }

}
