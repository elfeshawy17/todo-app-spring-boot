package org.mytodoapp.todo.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mytodoapp.todo.task.dto.TaskRequestDto;
import org.mytodoapp.todo.task.dto.TaskResponseDto;
import org.mytodoapp.todo.task.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequestDto dto);

    @Mapping(source = "user.id", target = "userId")
    TaskResponseDto toResponseDto (Task entity);

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateTaskFromDto(TaskRequestDto dto, @MappingTarget Task entity);

}
