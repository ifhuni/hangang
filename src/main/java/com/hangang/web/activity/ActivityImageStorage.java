package com.hangang.web.activity;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ActivityImageStorage {

    private final Path uploadRoot;

    public ActivityImageStorage(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir, "activities").toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String save(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String filename = UUID.randomUUID() + extractExtension(file.getOriginalFilename());
        Path target = uploadRoot.resolve(filename);
        try {
            file.transferTo(target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return "/uploads/activities/" + filename;
    }

    public void delete(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }
        String filename = imagePath.substring(imagePath.lastIndexOf('/') + 1);
        try {
            Files.deleteIfExists(uploadRoot.resolve(filename));
        } catch (IOException ignored) {
            // best-effort cleanup; a leftover file on disk is not worth failing the request
        }
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        int dot = originalFilename.lastIndexOf('.');
        return dot >= 0 ? originalFilename.substring(dot) : "";
    }
}
