package com.project.bakerymanagementsystem.controller;

import com.project.bakerymanagementsystem.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> getProfileDocument(@PathVariable String fileName) {
        try {

            String uploadDir = fileService.uploadDir();

            Path documentPath = Paths.get(uploadDir, fileName);
            Resource resource = new UrlResource(documentPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                MediaType mediaType = fileService.determineMediaType(fileName);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
