package com.example.orbi.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "avaliacoes")
public class AvaliacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @Column(nullable = false)
    private UUID idConteudo;

    @Column(nullable = false)
    private Boolean avaliacao;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;

    @PrePersist
    protected void onCreate() {
        if (this.dataAvaliacao == null) {
            this.dataAvaliacao = LocalDateTime.now();
        }
    }
}

