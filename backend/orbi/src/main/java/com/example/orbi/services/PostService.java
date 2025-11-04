package com.example.orbi.services;

import com.example.orbi.dto.PostRequestDTO;
import com.example.orbi.dto.PostResponseDTO;
import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.PostRepository;
import com.example.orbi.repositories.UsuarioRepository;
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

        return mapToResponseDTO(postSalvo);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> listarPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    private PostResponseDTO mapToResponseDTO(PostModel post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitulo(),
                post.getConteudo(),
                post.getAutor().getUsername(),
                post.getDataCriacao(),
                post.getCurtidas() != null ? post.getCurtidas().size() : 0,
                post.getDeslikes() != null ? post.getDeslikes().size() : 0
        );
    }


}
