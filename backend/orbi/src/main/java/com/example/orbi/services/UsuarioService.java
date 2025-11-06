package com.example.orbi.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
                dto.setSenha(null);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUsuarioPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        entityManager.createNativeQuery("DELETE FROM post_likes WHERE post_id IN (SELECT id FROM posts WHERE autor_id = :usuarioId)")
                .setParameter("usuarioId", usuario.getId())
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM post_likes WHERE usuario_id = :usuarioId")
                .setParameter("usuarioId", usuario.getId())
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM post_dislikes WHERE post_id IN (SELECT id FROM posts WHERE autor_id = :usuarioId)")
                .setParameter("usuarioId", usuario.getId())
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM post_dislikes WHERE usuario_id = :usuarioId")
                .setParameter("usuarioId", usuario.getId())
                .executeUpdate();

        entityManager.createNativeQuery("DELETE FROM posts WHERE autor_id = :usuarioId")
                .setParameter("usuarioId", usuario.getId())
                .executeUpdate();

        // Passo 6: Deletar o usuário
        usuarioRepository.delete(usuario);
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
