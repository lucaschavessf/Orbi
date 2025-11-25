package com.example.orbi.dto;

import jakarta.validation.constraints.Size;

public record AtualizarPerfilRequestDTO(
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 500, message = "Bio deve ter no máximo 500 caracteres")
        String bio
) {
}
