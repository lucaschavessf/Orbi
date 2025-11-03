package com.example.orbi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuscaResponseDTO {
    private UUID id;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private AutorDTO autor;
    private Integer totalCurtidas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AutorDTO {
        private UUID id;
        private String username;
        private String nome;
        private String fotoPerfil;
    }
}

