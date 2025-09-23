package org.mytodoapp.todo.security.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mytodoapp.todo.security.auth.dto.AuthRequestDto;
import org.mytodoapp.todo.security.auth.dto.AuthResponseDto;
import org.mytodoapp.todo.security.auth.dto.RegisterRequestDto;
import org.mytodoapp.todo.security.auth.mapper.AuthMapper;
import org.mytodoapp.todo.security.auth.service.AuthService;
import org.mytodoapp.todo.security.util.JwtUtil;
import org.mytodoapp.todo.shared.exception.DuplicateResourceException;
import org.mytodoapp.todo.user.entity.User;
import org.mytodoapp.todo.user.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto dto) throws DuplicateResourceException {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = authMapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        Map<String, Object> claims = getClaims(user);
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), claims);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return createAuthResDto(user, accessToken, refreshToken);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, Object> claims = getClaims(user);
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), claims);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return createAuthResDto(user, accessToken, refreshToken);
    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken) {
        String email = jwtUtil.extractEmail(refreshToken);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtUtil.isTokenValid(refreshToken, email)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        Map<String, Object> claims = getClaims(user);
        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), claims);

        return createAuthResDto(user, newAccessToken, refreshToken);
    }

    private Map<String, Object> getClaims(User user) {
        return Map.of(
                "role", user.getRole().name(),
                "userId", user.getId()
        );
    }

    private AuthResponseDto createAuthResDto(User user, String accessToken, String refreshToken) {
        return new AuthResponseDto(
                accessToken,
                refreshToken,
                "Bearer",
                jwtUtil.getAccessExpirationMillis(),
                user.getEmail(),
                user.getRole().name()
        );
    }

}
