package com.example.orbi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(
        @NotBlank(message = "O título não pode estar vazio")
        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "O conteúdo não pode estar vazio")
        @Size(max = 5000, message = "O conteúdo deve ter no máximo 5000 caracteres")
        String conteudo,

        @NotBlank(message = "O username do autor é obrigatório")
        String usernameAutor
) {}
