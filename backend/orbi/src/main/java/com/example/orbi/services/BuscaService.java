package com.example.orbi.services;

import com.example.orbi.dto.BuscaResponseDTO;
import com.example.orbi.models.PostModel;
import com.example.orbi.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuscaService {

    @Autowired
    private PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<BuscaResponseDTO> buscarPosts(String texto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostModel> postsPage = postRepository.buscarPorTexto(texto, pageable);

        return postsPage.map(this::convertToDTO);
    }

    private BuscaResponseDTO convertToDTO(PostModel post) {
        BuscaResponseDTO dto = new BuscaResponseDTO();
        dto.setId(post.getId());
        dto.setTitulo(post.getTitulo());
        dto.setConteudo(post.getConteudo());
        dto.setDataCriacao(post.getDataCriacao());
        dto.setTotalCurtidas(post.getCurtidas() != null ? post.getCurtidas().size() : 0);

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

