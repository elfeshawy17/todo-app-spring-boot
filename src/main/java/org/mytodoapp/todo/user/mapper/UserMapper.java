package org.mytodoapp.todo.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mytodoapp.todo.user.dto.UserCreateDto;
import org.mytodoapp.todo.user.dto.UserResponseDto;
import org.mytodoapp.todo.user.dto.UserUpdateDto;
import org.mytodoapp.todo.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserCreateDto dto);

    UserResponseDto toResponseDto(User entity);

    @Mapping(target = "id",  ignore = true)
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User entity);

}
