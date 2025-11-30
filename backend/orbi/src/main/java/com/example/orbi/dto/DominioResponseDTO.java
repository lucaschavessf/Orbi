package com.example.orbi.dto;

import java.util.UUID;

public record DominioResponseDTO(
        UUID id,
        String dominio,
        String instituicao
) {}

