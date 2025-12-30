package com.ihajji.backend.posts.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public record PostRequestDTO(
    Long ID,
    String title,
    String content,
    List<MultipartFile> mediaFiles
) {}