package com.example.orbi.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
@Table(name = "posts")
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private UsuarioModel autor;
    
    @Column(length = 512)
    private String urlArquivo;

    @Column(name = "editado", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean editado = false;

    @Column(name = "data_hora_edicao")
    private LocalDateTime dataHoraEdicao;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_favoritos",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<UsuarioModel> favoritos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
        if (this.editado == null) {
            this.editado = false;
        }
    }
}
