package com.example.orbi.services;

import com.azure.core.annotation.Post;
import com.example.orbi.dto.PostRequestDTO;
import com.example.orbi.dto.PostResponseDTO;
import com.example.orbi.dto.PostUpdateRequestDTO;
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
import java.time.LocalDateTime;
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

    private void validarInstituicaoPost(PostModel post, UsuarioModel usuario) {
        if (usuario.getInstituicao() == null) {
            throw new RuntimeException("Usuário não possui instituição associada");
        }

        if (post.getAutor().getInstituicao() == null) {
            throw new RuntimeException("Autor do post não possui instituição associada");
        }

        if (!post.getAutor().getInstituicao().getId().equals(usuario.getInstituicao().getId())) {
            throw new RuntimeException("Acesso negado: Este post pertence a outra instituição");
        }
    }

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

        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getInstituicao() == null) {
            throw new RuntimeException("Usuário não possui instituição associada");
        }

        return postRepository.findByAutor_Instituicao_Id(usuario.getInstituicao().getId(), pageable)
                .map(post -> mapToResponseDTO(post, usuario));
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> listarPostsUsuario(Pageable pageable, String username) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }

        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getInstituicao() == null ||
                !usuario.getInstituicao().getId().equals(usuario.getInstituicao().getId())) {
            throw new RuntimeException("Acesso negado: Este perfil pertence a outra instituição");
        }

        Page<PostModel> page = postRepository.findByAutor_Id(usuario.getId(), pageable);
        return page.map(post -> mapToResponseDTO(post, usuario));
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> buscarPosts(String texto, Pageable pageable, String username) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }

        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getInstituicao() == null) {
            throw new RuntimeException("Usuário não possui instituição associada");
        }

        Pageable pageableWithoutSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<PostModel> postsPage = postRepository.buscarPorTextoEInstituicao(
                texto,
                usuario.getInstituicao().getId(),
                pageableWithoutSort
        );
        return postsPage.map(post -> mapToResponseDTO(post, usuario));
    }

    @Transactional
    public PostResponseDTO toggleAvaliar(UUID postId, String username, Boolean avaliacao) {
        Objects.requireNonNull(postId, "O ID do post não pode ser nulo");

        UsuarioModel user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        validarInstituicaoPost(post, user);

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
                post.getUrlArquivo(),
                post.getEditado(),
                post.getDataHoraEdicao()
        );
    }

    @Transactional
    public PostResponseDTO toggleFavorito(UUID postId, String username) {
        Objects.requireNonNull(postId, "O ID do post não pode ser nulo");

        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        UsuarioModel user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        validarInstituicaoPost(post, user);

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

        if (usuario.getInstituicao() == null) {
            throw new RuntimeException("Usuário não possui instituição associada");
        }

        Page<PostModel> favoritos = postRepository.findByFavoritosContainingAndAutor_Instituicao_Id(
                usuario,
                usuario.getInstituicao().getId(),
                pageable
        );
        return favoritos.map(post -> mapToResponseDTO(post, usuario));
    }

    public PostResponseDTO atualizarPost(UUID id, PostUpdateRequestDTO dto) {

        PostModel post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        if(dto.usernameAutor() == null || !dto.usernameAutor().equals(post.getAutor().getUsername())) {
            throw new RuntimeException("Apenas o autor do post pode editá-lo");
        }
        post.setTitulo(dto.titulo());
        post.setConteudo(dto.conteudo());

        if (dto.urlArquivo() != null && !dto.urlArquivo().isBlank()) {
            post.setUrlArquivo(dto.urlArquivo());
        }

        post.setEditado(true);
        post.setDataHoraEdicao(LocalDateTime.now());

        postRepository.save(post);

        return mapToResponseDTO(post, post.getAutor());
    }

    @Transactional(readOnly = true)
    public PostResponseDTO listarPostPorId(UUID id, String username) {
        PostModel post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        validarInstituicaoPost(post, usuario);

        return mapToResponseDTO(post, usuario);
    }

    public void excluirPost(UUID postId, String usernameAutenticado) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado."));

        if (!post.getAutor().getUsername().equals(usernameAutenticado)) {
            throw new RuntimeException("Você não tem permissão para excluir este post.");
        }

        postRepository.delete(post);
    }
}
