package com.example.orbi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setEmail(usuarioDTO.getEmail());

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuario.setTipo(usuarioDTO.getTipo());
        usuario.setCurso(usuarioDTO.getCurso());
        usuario.setBio(usuarioDTO.getBio());
        usuario.setFotoPerfil(usuarioDTO.getFotoPerfil());

        UsuarioModel savedUsuario = usuarioRepository.save(usuario);

        UsuarioDTO response = new UsuarioDTO(savedUsuario);
        response.setSenha(null);

        return response;
    }

    public List<UsuarioDTO> listarUsuarios() {
    return usuarioRepository.findAll().stream()
            .map(usuario -> {
                UsuarioDTO dto = new UsuarioDTO(usuario);
                dto.setSenha(null); // zera a senha
                return dto;
            })
            .collect(Collectors.toList());
    }


    @Transactional
    public void deleteUsuarioPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }


    public UsuarioDTO buscarPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        UsuarioDTO dto = new UsuarioDTO(usuario);
        dto.setSenha(null);
        return dto;
    }




}
