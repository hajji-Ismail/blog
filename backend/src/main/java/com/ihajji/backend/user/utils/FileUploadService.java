package com.ihajji.backend.user.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {

    private final Cloudinary cloudinary;

    // Cloudinary bean is injected here
    public FileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
public String uploadFile(MultipartFile file, String folderName) throws IOException {

    if (file == null || file.isEmpty()) {
               return "";

    }

    String contentType = file.getContentType();

    if (contentType == null) {
        return "";
    }

    String resourceType;

    if (contentType.startsWith("image/")) {
        resourceType = "image";

    } else if (contentType.startsWith("video/")) {
        resourceType = "video";

    } else {
               return "";

    }

    try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(), // safer for both images and videos
                    ObjectUtils.asMap(
                            "folder", folderName,
                            "resource_type", resourceType
                    )
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException | RuntimeException e) {
            // log the error, but do NOT crash
            System.err.println("Cloudinary upload failed: " + e.getMessage());
            return ""; // fail silently
        }

}
}