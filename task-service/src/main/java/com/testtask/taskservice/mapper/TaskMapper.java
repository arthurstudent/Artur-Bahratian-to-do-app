package com.testtask.taskservice.mapper;

import com.testtask.taskservice.model.dto.data.AttachmentDto;
import com.testtask.taskservice.model.dto.data.TaskDto;
import com.testtask.taskservice.model.dto.data.TaskDtoSingle;
import com.testtask.taskservice.model.entity.TaskEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    
    private final ModelMapper modelMapper;
    
    public TaskDto toTaskDto(@NonNull TaskEntity taskEntity) {
        return fromTaskEntityToTaskDto(taskEntity, TaskDto.class);
    }

    public TaskDtoSingle toTaskDtoSingle(@NonNull TaskEntity taskEntity) {
        return fromTaskEntityToTaskDto(taskEntity, TaskDtoSingle.class);
    }

    public TaskDto toTaskDto(@NonNull TaskEntity taskEntity, @NonNull List<AttachmentDto> fileNames) {
        TaskDto taskDto = fromTaskEntityToTaskDto(taskEntity, TaskDto.class);
        taskDto.setFiles(fileNames);
        return taskDto;
    }

    private <T> T fromTaskEntityToTaskDto(TaskEntity taskEntity, Class<T> targetClass) {
        return modelMapper.map(taskEntity, targetClass);
    }
}
