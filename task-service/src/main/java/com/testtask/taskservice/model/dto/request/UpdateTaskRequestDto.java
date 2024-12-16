package com.testtask.taskservice.model.dto.request;

import com.testtask.taskservice.model.entity.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskRequestDto {

    private final String taskName;

    private final String taskDescription;

    private final TaskStatus taskStatus;
}
