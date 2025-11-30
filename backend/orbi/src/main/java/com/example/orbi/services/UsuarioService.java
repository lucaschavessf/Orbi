package com.example.orbi.services;

import com.example.orbi.dto.AtualizarPerfilRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.DominioRepository;
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
    private DominioRepository dominioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }
        String email = usuarioDTO.getEmail();
        String dominio = email.substring(email.indexOf("@") + 1).trim();
        if (!dominioRepository.existsByDominioIgnoreCase(dominio)) {
            throw new RuntimeException("O domínio do email não está vinculado a nenhuma instituição cadastrada.");
        }
        UsuarioModel usuario = new UsuarioModel();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setNome(usuarioDTO.getNome());
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
                dto.setSenha(null);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUsuarioPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UUID usuarioId = usuario.getId();

        entityManager.createNativeQuery("DELETE FROM avaliacoes WHERE usuario_id = :usuarioId")
                .setParameter("usuarioId", usuarioId)
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM comentarios WHERE autor_id = :usuarioId")
                .setParameter("usuarioId", usuarioId)
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM post_favoritos WHERE usuario_id = :usuarioId")
                .setParameter("usuarioId", usuarioId)
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM post_favoritos WHERE post_id IN (SELECT id FROM posts WHERE autor_id = :usuarioId)")
                .setParameter("usuarioId", usuarioId)
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM avaliacoes WHERE id_conteudo IN (SELECT id FROM posts WHERE autor_id = :usuarioId)")
                .setParameter("usuarioId", usuarioId)
                .executeUpdate();

        usuarioRepository.delete(usuario);
    }

    @Transactional
    public UsuarioDTO atualizarPerfil(String username, AtualizarPerfilRequestDTO dto) {

        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuario.setNome(dto.nome());
        }

        if (dto.bio() != null) {
            usuario.setBio(dto.bio());
        }

        if (dto.fotoPerfil() != null) {
            usuario.setFotoPerfil(dto.fotoPerfil());
        }

        if (dto.username() != null && !dto.username().isBlank()
                && !dto.username().equals(usuario.getUsername())) {

            if (usuarioRepository.existsByUsername(dto.username())) {
                throw new IllegalArgumentException("Nome de usuário já está em uso");
            }

            usuario.setUsername(dto.username());
        }

        if (dto.senhaAtual() != null && dto.novaSenha() != null) {

            boolean correta = passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha());

            if (!correta) {
                throw new IllegalArgumentException("Senha atual incorreta");
            }

            usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        }

        UsuarioModel salvo = usuarioRepository.save(usuario);

        UsuarioDTO response = new UsuarioDTO(salvo);
        response.setSenha(null);

        return response;
    }






    public UsuarioDTO buscarPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        UsuarioDTO dto = new UsuarioDTO(usuario);
        dto.setSenha(null);
        return dto;
    }

    public UsuarioDTO verificarLogin(String username, String senhaRecebida) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        boolean senhaCorreta = passwordEncoder.matches(senhaRecebida, usuario.getSenha());

        if (!senhaCorreta) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        UsuarioDTO dto = new UsuarioDTO(usuario);
        dto.setSenha(null);

        return dto;
    }
}
