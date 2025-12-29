package com.ihajji.backend.posts.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public record PostRequestDTO(
    String title,
    String content,
    List<MultipartFile> mediaFiles
) {}