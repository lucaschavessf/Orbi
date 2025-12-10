package com.example.orbi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AtualizarPerfilRequestDTO(

        @Size(max = 25, message = "Nome de usuário deve ter no máximo 25 caracteres")
        String username,

        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 500, message = "Bio deve ter no máximo 500 caracteres")
        String bio,

        @Size(max = 512, message = "URL muito longa")
        String fotoPerfil,

        String senhaAtual,
        @Size(min = 6, message = "A nova senha deve ter pelo menos 6 caracteres")
        String novaSenha
) {}

