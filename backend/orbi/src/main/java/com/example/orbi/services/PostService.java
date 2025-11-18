package com.example.orbi.services;

import com.example.orbi.dto.PostRequestDTO;
import com.example.orbi.dto.PostResponseDTO;
import com.example.orbi.models.AvaliacaoModel;
import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.AvaliacaoRepository;
import com.example.orbi.repositories.ComentarioRepository;
import com.example.orbi.repositories.PostRepository;
import com.example.orbi.repositories.UsuarioRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Objects;

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

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    public PostResponseDTO criarPost(PostRequestDTO dto) {
        UsuarioModel autor = usuarioRepository.findByUsername(dto.usernameAutor())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PostModel post = new PostModel();
        post.setTitulo(dto.titulo());
        post.setConteudo(dto.conteudo());
        post.setAutor(autor);
        post.setUrlArquivo(dto.urlArquivo());

        PostModel postSalvo = postRepository.save(post);
        return mapToResponseDTO(postSalvo, autor);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> listarPosts(Pageable pageable, String username) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);
        return postRepository.findAll(pageable)
                .map(post -> mapToResponseDTO(post, usuarioOpt.orElse(null)));
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> buscarPosts(String texto, Pageable pageable, String username) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);

        Pageable pageableWithoutSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<PostModel> postsPage = postRepository.buscarPorTexto(texto, pageableWithoutSort);
        return postsPage.map(post -> mapToResponseDTO(post, usuarioOpt.orElse(null)));
    }

    @Transactional
    public PostResponseDTO toggleAvaliar(UUID postId, String username, Boolean avaliacao) {
        Objects.requireNonNull(postId, "O ID do post não pode ser nulo");

        UsuarioModel user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        Optional<AvaliacaoModel> avaliacaoExistente =
                avaliacaoRepository.findByUsuarioIdAndIdConteudo(user.getId(), postId);

        if (avaliacaoExistente.isPresent()) {
            AvaliacaoModel av = avaliacaoExistente.get();

            if (av.getAvaliacao().equals(avaliacao)) {
                avaliacaoRepository.delete(av);
            } else {
                av.setAvaliacao(avaliacao);
                avaliacaoRepository.save(av);
            }
        } else {
            AvaliacaoModel nova = new AvaliacaoModel();
            nova.setUsuario(user);
            nova.setIdConteudo(postId);
            nova.setAvaliacao(avaliacao);
            avaliacaoRepository.save(nova);
        }

        return mapToResponseDTO(post, user);
    }

    private PostResponseDTO mapToResponseDTO(PostModel post, UsuarioModel usuario) {
        long totalCurtidas = avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), true);
        long totalDeslikes = avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), false);
        long totalComentarios = comentarioRepository.countByPostId(post.getId());

        boolean curtido = false;
        boolean descurtido = false;
        boolean favoritado = false;

        if (usuario != null) {
            Optional<AvaliacaoModel> avaliacaoOpt =
                    avaliacaoRepository.findByUsuarioIdAndIdConteudo(usuario.getId(), post.getId());

            if (avaliacaoOpt.isPresent()) {
                curtido = Boolean.TRUE.equals(avaliacaoOpt.get().getAvaliacao());
                descurtido = Boolean.FALSE.equals(avaliacaoOpt.get().getAvaliacao());
            }

            favoritado = post.getFavoritos().contains(usuario);
        }

        return new PostResponseDTO(
                post.getId(),
                post.getTitulo(),
                post.getConteudo(),
                post.getAutor().getUsername(),
                post.getDataCriacao(),
                (int) totalCurtidas,
                (int) totalDeslikes,
                curtido,
                descurtido,
                favoritado,
                (int) totalComentarios,
                post.getUrlArquivo()
        );
    }

    @Transactional
    public PostResponseDTO toggleFavorito(UUID postId, String username) {
        Objects.requireNonNull(postId, "O ID do post não pode ser nulo");

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
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }

        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Page<PostModel> favoritos = postRepository.findByFavoritosContaining(usuario, pageable);
        return favoritos.map(post -> mapToResponseDTO(post, usuario));
    }
}
