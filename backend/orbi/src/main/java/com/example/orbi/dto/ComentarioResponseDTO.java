package com.example.orbi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioResponseDTO {
    private UUID id;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private String usernameAutor;
    private String nomeAutor;
    private String fotoPerfilAutor;
    private int totalCurtidas;
    private int totalDeslikes;
    private boolean curtido;
    private boolean descurtido;
    private UUID comentarioPaiId;
    private List<ComentarioResponseDTO> respostas = new ArrayList<>();
}