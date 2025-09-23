package org.mytodoapp.todo.security.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mytodoapp.todo.security.auth.dto.AuthResponseDto;
import org.mytodoapp.todo.security.auth.dto.RegisterRequestDto;
import org.mytodoapp.todo.user.entity.User;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    @Mapping(target = "role", constant = "USER")
    User toUser(RegisterRequestDto dto);

}
