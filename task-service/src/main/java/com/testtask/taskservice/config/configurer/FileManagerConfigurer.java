package com.testtask.taskservice.config.configurer;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FileManagerConfigurer {

    @Value("${files.path}")
    private String storeFilePath;
}
