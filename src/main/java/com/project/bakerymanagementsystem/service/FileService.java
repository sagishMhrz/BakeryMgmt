package com.project.bakerymanagementsystem.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Service
public class FileService {

        public String uploadDir() throws IOException {
            String uploadDir = "static/image/";

            Resource resource = new FileSystemResource(uploadDir);
            String resourcePath = URLDecoder.decode(resource.getFile().getAbsolutePath(), "UTF-8");
            resourcePath = resourcePath.replace(File.separator, "/");
            File dir = new File(resourcePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return resourcePath;
        }

        public String saveFile(MultipartFile file) throws IOException {
            String uploadDir = uploadDir();

            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDir, fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName;
        }

        public String generateUniqueFileName(String originalFileName) {
            String fileExtension = FilenameUtils.getExtension(originalFileName);
            String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
            return uniqueFileName;
        }

        public MediaType determineMediaType(String fileName) {
            String[] parts = fileName.split("\\.");
            String fileExtension = parts[parts.length - 1].toLowerCase();
            switch (fileExtension) {
                case "pdf":
                    return MediaType.APPLICATION_PDF;
                case "jpg":
                case "jpeg":
                    return MediaType.IMAGE_JPEG;
                case "png":
                    return MediaType.IMAGE_PNG;
                default:
                    return MediaType.APPLICATION_OCTET_STREAM;
            }
        }
    }

