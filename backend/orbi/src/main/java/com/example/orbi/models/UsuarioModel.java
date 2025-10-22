package com.example.orbi.models;

import java.io.Serializable;
import java.util.UUID;

import com.example.orbi.models.enums.TipoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class UsuarioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false, length = 25)
    private String username;
    @Column(nullable = false, length = 256)
    private String nome;
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;
    @Column(unique = true, nullable = false, length = 256)
    private String email;
    @Column(nullable = false, length = 256)
    private String senha;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;
    @Column(nullable = false, length = 256)
    private String curso;
    @Column(length = 512)
    private String bio;
    @Column(length = 512)
    private String fotoPerfil;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }
    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getCurso() {
        return curso;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getFotoPerfil() {
        return fotoPerfil;
    }
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
}
