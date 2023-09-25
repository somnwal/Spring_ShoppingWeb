package com.shopping.backend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    public static void saveFile(String dir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(dir);

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream is = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
