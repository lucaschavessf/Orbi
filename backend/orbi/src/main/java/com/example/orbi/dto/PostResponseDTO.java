package com.example.orbi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponseDTO(
        UUID id,
        String titulo,
        String conteudo,
        String usernameAutor,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dataCriacao,
        Integer totalCurtidas,
        Integer totalDeslikes,
        Boolean curtidoPeloUsuario,
        Boolean descurtidoPeloUsuario,
        Boolean favoritadoPeloUsuario,
        Integer totalComentarios,
        String urlArquivo
) {}

