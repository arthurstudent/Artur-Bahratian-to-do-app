package com.testtask.taskservice.model.dto.request;

import com.testtask.taskservice.model.entity.TaskStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserTasksRequestDto {

    private TaskStatus taskStatus;

    private String taskName;

    @Min(0)
    @NotNull
    private Integer page;

    @Min(1)
    @NotNull
    private Integer pageSize;
}
