package com.example.orbi.services;

import com.example.orbi.dto.PostRequestDTO;
import com.example.orbi.dto.PostResponseDTO;
import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.PostRepository;
import com.example.orbi.repositories.UsuarioRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public PostResponseDTO criarPost(PostRequestDTO dto) {
        UsuarioModel autor = usuarioRepository.findByUsername(dto.usernameAutor())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PostModel post = new PostModel();
        post.setTitulo(dto.titulo());
        post.setConteudo(dto.conteudo());
        post.setAutor(autor);

        PostModel postSalvo = postRepository.save(post);

        return mapToResponseDTO(postSalvo, autor);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> listarPosts(Pageable pageable, String username) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);

        return postRepository.findAll(pageable)
                .map(post -> mapToResponseDTO(post, usuarioOpt.orElse(null)));
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> buscarPosts(String texto, Pageable pageable, String username) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);

        Pageable pageableWithoutSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<PostModel> postsPage = postRepository.buscarPorTexto(texto, pageableWithoutSort);

        return postsPage.map(post -> mapToResponseDTO(post, usuarioOpt.orElse(null)));
    }

    @Transactional
    public PostResponseDTO toggleLike(UUID postId, String username) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        UsuarioModel user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Set<UsuarioModel> curtidas = post.getCurtidas();
        Set<UsuarioModel> deslikes = post.getDeslikes();

        if (curtidas.contains(user)) {
            curtidas.remove(user);
        } else {
            deslikes.remove(user);
            curtidas.add(user);
        }

        PostModel saved = postRepository.save(post);
        return mapToResponseDTO(saved, user);
    }

    @Transactional
    public PostResponseDTO toggleDislike(UUID postId, String username) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        UsuarioModel user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Set<UsuarioModel> deslikes = post.getDeslikes();
        Set<UsuarioModel> curtidas = post.getCurtidas();

        if (deslikes.contains(user)) {
            deslikes.remove(user);
        } else {
            curtidas.remove(user);
            deslikes.add(user);
        }

        PostModel saved = postRepository.save(post);
        return mapToResponseDTO(saved, user);
    }

    private PostResponseDTO mapToResponseDTO(PostModel post, UsuarioModel usuario) {
        boolean curtido = false;
        boolean descurtido = false;
        boolean favoritado = false;

        if (usuario != null) {
            curtido = post.getCurtidas().contains(usuario);
            descurtido = post.getDeslikes().contains(usuario);
            favoritado = post.getFavoritos().contains(usuario);
        }

        return new PostResponseDTO(
                post.getId(),
                post.getTitulo(),
                post.getConteudo(),
                post.getAutor().getUsername(),
                post.getDataCriacao(),
                post.getCurtidas() != null ? post.getCurtidas().size() : 0,
                post.getDeslikes() != null ? post.getDeslikes().size() : 0,
                curtido,
                descurtido,
                favoritado
        );
    }

    @Transactional
    public PostResponseDTO toggleFavorito(UUID postId, String username) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        UsuarioModel user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Set<UsuarioModel> favoritos = post.getFavoritos();

        if (favoritos.contains(user)) {
            favoritos.remove(user);
        } else {
            favoritos.add(user);
        }

        PostModel saved = postRepository.save(post);
        return mapToResponseDTO(saved, user);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> listarFavoritos(String username, Pageable pageable) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Page<PostModel> favoritos = postRepository.findByFavoritosContaining(usuario, pageable);

        return favoritos.map(post -> mapToResponseDTO(post, usuario));
    }
}
