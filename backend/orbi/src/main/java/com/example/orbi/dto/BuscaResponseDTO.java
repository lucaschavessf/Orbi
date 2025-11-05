package com.example.orbi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataCriacao;
    private AutorDTO autor;
    private Integer totalCurtidas;
    private Integer totalDeslikes;
    private Boolean curtidoPeloUsuario;
    private Boolean descurtidoPeloUsuario;

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
