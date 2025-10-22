package com.example.orbi.dto;

import com.example.orbi.models.UsuarioModel;
import com.example.orbi.models.enums.TipoUsuario;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String username;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private TipoUsuario tipo;
    private String curso;
    private String bio;
    private String fotoPerfil;

    public UsuarioDTO(UsuarioModel model) {
        this.username = model.getUsername();
        this.nome = model.getNome();
        this.cpf = model.getCpf();
        this.email = model.getEmail();
        this.senha = model.getSenha();
        this.tipo = model.getTipo();
        this.curso = model.getCurso();
        this.bio = model.getBio();
        this.fotoPerfil = model.getFotoPerfil();
    }

    public UsuarioModel toModel() {
        UsuarioModel model = new UsuarioModel();
        model.setUsername(username);
        model.setNome(nome);
        model.setCpf(cpf);
        model.setEmail(email);
        model.setSenha(senha);
        model.setTipo(tipo);
        model.setCurso(curso);
        model.setBio(bio);
        model.setFotoPerfil(fotoPerfil);
        return model;
    }
}