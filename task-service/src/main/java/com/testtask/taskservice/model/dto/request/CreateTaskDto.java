package com.testtask.taskservice.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class CreateTaskDto {

    private String taskName;

    private String description;

    private List<MultipartFile> files = new ArrayList<>();

    private Long userId;
}
