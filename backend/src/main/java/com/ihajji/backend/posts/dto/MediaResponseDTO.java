package com.ihajji.backend.posts.dto;

public record MediaResponseDTO(
    Long id,
    String fileUrl,
    String fileType
) {}