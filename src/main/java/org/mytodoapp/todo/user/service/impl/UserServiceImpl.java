package org.mytodoapp.todo.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.mytodoapp.todo.shared.exception.DuplicateResourceException;
import org.mytodoapp.todo.shared.exception.RecordNotFoundException;
import org.mytodoapp.todo.user.dto.UserCreateDto;
import org.mytodoapp.todo.user.dto.UserResponseDto;
import org.mytodoapp.todo.user.dto.UserUpdateDto;
import org.mytodoapp.todo.user.entity.User;
import org.mytodoapp.todo.user.mapper.UserMapper;
import org.mytodoapp.todo.user.repo.UserRepo;
import org.mytodoapp.todo.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto add(UserCreateDto dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        }

        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists: " + dto.getUsername());
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto update(Long id, UserUpdateDto dto) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User", id));

        userMapper.updateUserFromDto(dto, existingUser);
        userRepo.save(existingUser);

        return userMapper.toResponseDto(existingUser);
    }

    @Override
    public void delete(Long id) {
        userRepo.findById(id).orElseThrow(() -> new RecordNotFoundException("User", id));
        userRepo.deleteById(id);
    }

    @Override
    public UserResponseDto findById(Long id) {
        return userRepo.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new RecordNotFoundException("User", id));
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepo.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
