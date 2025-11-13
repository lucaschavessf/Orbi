package com.example.orbi.services;

import com.example.orbi.dto.ComentarioRequestDTO;
import com.example.orbi.dto.ComentarioResponseDTO;
import com.example.orbi.models.AvaliacaoModel;
import com.example.orbi.models.ComentarioModel;
import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.AvaliacaoRepository;
import com.example.orbi.repositories.ComentarioRepository;
import com.example.orbi.repositories.PostRepository;
import com.example.orbi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Transactional
    public ComentarioResponseDTO criarComentario(ComentarioRequestDTO dto) {
        UsuarioModel autor = usuarioRepository.findByUsername(dto.usernameAutor())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PostModel post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        ComentarioModel comentario = new ComentarioModel();
        comentario.setConteudo(dto.conteudo());
        comentario.setAutor(autor);
        comentario.setPost(post);

        if (dto.comentarioPaiId() != null) {
            ComentarioModel comentarioPai = comentarioRepository.findById(dto.comentarioPaiId())
                    .orElseThrow(() -> new RuntimeException("Comentário pai não encontrado"));
            comentario.setComentarioPai(comentarioPai);
        }

        ComentarioModel salvo = comentarioRepository.save(comentario);
        return mapToResponseDTO(salvo, autor);
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> listarComentariosPorPost(UUID postId, String username) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(username);

        List<ComentarioModel> comentarios = comentarioRepository
                .findByPostIdAndComentarioPaiIsNull(postId);

        return comentarios.stream()
                .map(comentario -> {
                    ComentarioResponseDTO dto = mapToResponseDTO(comentario, usuarioOpt.orElse(null));
                    carregarRespostas(dto, usuarioOpt.orElse(null));
                    return dto;
                })
                .sorted(Comparator.comparingInt(ComentarioResponseDTO::getTotalCurtidas).reversed())
                .collect(Collectors.toList());
    }

    private void carregarRespostas(ComentarioResponseDTO comentarioDTO, UsuarioModel usuario) {
        List<ComentarioModel> respostas = comentarioRepository
                .findByComentarioPaiIdOrderByDataCriacaoAsc(comentarioDTO.getId());

        List<ComentarioResponseDTO> respostasDTO = respostas.stream()
                .map(resposta -> {
                    ComentarioResponseDTO dto = mapToResponseDTO(resposta, usuario);
                    carregarRespostas(dto, usuario);
                    return dto;
                })
                .collect(Collectors.toList());

        comentarioDTO.setRespostas(respostasDTO);
    }


    @Transactional
    public ComentarioResponseDTO avaliarComentario(UUID comentarioId, String username, Boolean avaliacao) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ComentarioModel comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        Optional<AvaliacaoModel> avaliacaoExistente =
                avaliacaoRepository.findByUsuarioIdAndIdConteudo(usuario.getId(), comentarioId);

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
            nova.setUsuario(usuario);
            nova.setIdConteudo(comentarioId);
            nova.setAvaliacao(avaliacao);
            avaliacaoRepository.save(nova);
        }

        return mapToResponseDTO(comentario, usuario);
    }

    @Transactional
    public void deletarComentario(UUID comentarioId, String username) {
        ComentarioModel comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!comentario.getAutor().getId().equals(usuario.getId())) {
            throw new RuntimeException("Apenas o autor pode deletar o comentário");
        }

        comentarioRepository.delete(comentario);
    }

    private ComentarioResponseDTO mapToResponseDTO(ComentarioModel comentario, UsuarioModel usuario) {
        long totalCurtidas = avaliacaoRepository.countByIdConteudoAndAvaliacao(comentario.getId(), true);
        long totalDeslikes = avaliacaoRepository.countByIdConteudoAndAvaliacao(comentario.getId(), false);

        boolean curtido = false;
        boolean descurtido = false;

        if (usuario != null) {
            Optional<AvaliacaoModel> avaliacaoOpt =
                    avaliacaoRepository.findByUsuarioIdAndIdConteudo(usuario.getId(), comentario.getId());

            if (avaliacaoOpt.isPresent()) {
                curtido = Boolean.TRUE.equals(avaliacaoOpt.get().getAvaliacao());
                descurtido = Boolean.FALSE.equals(avaliacaoOpt.get().getAvaliacao());
            }
        }

        ComentarioResponseDTO dto = new ComentarioResponseDTO();
        dto.setId(comentario.getId());
        dto.setConteudo(comentario.getConteudo());
        dto.setDataCriacao(comentario.getDataCriacao());
        dto.setUsernameAutor(comentario.getAutor().getUsername());
        dto.setNomeAutor(comentario.getAutor().getNome());
        dto.setFotoPerfilAutor(comentario.getAutor().getFotoPerfil());
        dto.setTotalCurtidas((int) totalCurtidas);
        dto.setTotalDeslikes((int) totalDeslikes);
        dto.setCurtido(curtido);
        dto.setDescurtido(descurtido);

        if (comentario.getComentarioPai() != null) {
            dto.setComentarioPaiId(comentario.getComentarioPai().getId());
        }

        return dto;
    }
}