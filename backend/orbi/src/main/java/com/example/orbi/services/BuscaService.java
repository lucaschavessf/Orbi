package com.example.orbi.services;

import com.example.orbi.dto.BuscaResponseDTO;
import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.PostRepository;
import com.example.orbi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BuscaService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Page<BuscaResponseDTO> buscarPosts(String texto, int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostModel> postsPage = postRepository.buscarPorTexto(texto, pageable);

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);

        return postsPage.map(post -> convertToDTO(post, usuarioOpt.orElse(null)));
    }

    private BuscaResponseDTO convertToDTO(PostModel post, UsuarioModel usuario) {
        boolean curtido = false;
        boolean descurtido = false;

        if (usuario != null) {
            curtido = post.getCurtidas().contains(usuario);
            descurtido = post.getDeslikes().contains(usuario);
        }

        BuscaResponseDTO dto = new BuscaResponseDTO();
        dto.setId(post.getId());
        dto.setTitulo(post.getTitulo());
        dto.setConteudo(post.getConteudo());
        dto.setDataCriacao(post.getDataCriacao());
        dto.setTotalCurtidas(post.getCurtidas() != null ? post.getCurtidas().size() : 0);
        dto.setTotalDeslikes(post.getDeslikes() != null ? post.getDeslikes().size() : 0);
        dto.setCurtidoPeloUsuario(curtido);
        dto.setDescurtidoPeloUsuario(descurtido);

        if (post.getAutor() != null) {
            BuscaResponseDTO.AutorDTO autorDTO = new BuscaResponseDTO.AutorDTO();
            autorDTO.setId(post.getAutor().getId());
            autorDTO.setUsername(post.getAutor().getUsername());
            autorDTO.setNome(post.getAutor().getNome());
            autorDTO.setFotoPerfil(post.getAutor().getFotoPerfil());
            dto.setAutor(autorDTO);
        }

        return dto;
    }
}
