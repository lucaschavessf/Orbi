package com.example.orbi.dto;

public record SasTokenResponseDTO(
        String uploadUrl,
        String fileUrl,
        String fileName
) {}

