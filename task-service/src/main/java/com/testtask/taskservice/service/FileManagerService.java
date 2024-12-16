package com.testtask.taskservice.service;

import com.testtask.taskservice.config.configurer.FileManagerConfigurer;
import com.testtask.taskservice.exceptions.custom.FileProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileManagerService {

    private final FileManagerConfigurer fileManagerConfigurer;

    public void writeToFile(String filePath, byte[] content) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(content);
            fos.close();
        } catch (IOException e) {
            log.warn("Could not write to file {}", filePath, e);
            throw new FileProcessingException("Unable to write to file from provided data");
        }
    }

    public Resource getFileByName(@NonNull String fileName) {
        try {
            Path filePath = Paths.get(fileManagerConfigurer.getStoreFilePath() + fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.warn("File with name {} not found in the local storage", fileName);
                throw new FileProcessingException("Could not read file with name " + fileName);
            }
        } catch (MalformedURLException mal) {
            throw new FileProcessingException("Error: " + mal.getMessage());
        }
    }
}
