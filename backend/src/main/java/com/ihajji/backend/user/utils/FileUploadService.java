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
        try {
            // Options for upload: specify the target folder
            Map uploadResult = this.cloudinary.uploader().upload(
                file.getBytes(), 
                ObjectUtils.asMap("folder", folderName)
            );
            
            // Return the secure URL of the uploaded asset
            return uploadResult.get("secure_url").toString();
            
        } catch (IOException e) {
            // Log the exception or throw a custom runtime exception
            throw new IOException("Failed to upload file : " + e.getMessage());
        }
    }
}