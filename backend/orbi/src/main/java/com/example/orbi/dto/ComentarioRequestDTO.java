package com.example.orbi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ComentarioRequestDTO(
        @NotNull(message = "Username do autor é obrigatório")
        String usernameAutor,

        @NotBlank(message = "Conteúdo não pode estar vazio")
        @Size(min = 1, max = 2000, message = "Conteúdo deve ter entre 1 e 2000 caracteres")
        String conteudo,

        UUID postId,

        UUID comentarioPaiId
) {
}