package com.example.orbi.dto;

import java.util.List;

public record InstituicaoRequestDTO(
        String nome,
        List<String> dominios
) {}