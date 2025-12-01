package com.example.orbi.dto;

import java.util.List;
import java.util.UUID;

public record InstituicaoResponseDTO(
        UUID id,
        String nome,
        List<String> dominios
) {}