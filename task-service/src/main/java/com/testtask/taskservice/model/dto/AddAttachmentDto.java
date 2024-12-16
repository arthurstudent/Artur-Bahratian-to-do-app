package com.testtask.taskservice.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class AddAttachmentDto {

    private List<MultipartFile> files = new ArrayList<>();

    private Long taskId;

    private Long userId;
}
