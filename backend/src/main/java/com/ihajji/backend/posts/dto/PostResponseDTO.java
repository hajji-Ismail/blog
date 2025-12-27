package com.ihajji.backend.posts.dto;

import java.util.List;

public record PostResponseDTO(
    Long id,
    String title,
    String content,
    Long userId,            
    String authorName,      
    List<MediaResponseDTO> medias 
) {}