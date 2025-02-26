package com.proxy.task.mapper;


import com.proxy.task.dto.TaskDto;
import com.proxy.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);

    List<TaskDto> toDtoList(List<Task> tasks);

    Task toEntity(TaskDto taskDto);

    @Mapping(target = "id", ignore = true)
    void updateTask(TaskDto dto, @MappingTarget Task task);
}
