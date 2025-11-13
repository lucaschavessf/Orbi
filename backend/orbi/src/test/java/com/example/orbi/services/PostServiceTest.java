package com.example.orbi.services;

import com.example.orbi.dto.PostRequestDTO;
import com.example.orbi.dto.PostResponseDTO;
import com.example.orbi.models.AvaliacaoModel;
import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.models.enums.TipoUsuario;
import com.example.orbi.repositories.AvaliacaoRepository;
import com.example.orbi.repositories.PostRepository;
import com.example.orbi.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService - Testes Unitários")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @InjectMocks
    private PostService postService;

    private UsuarioModel autor;
    private UsuarioModel usuario;
    private PostModel post;
    private PostRequestDTO postRequestDTO;

    @BeforeEach
    void setUp() {
        autor = new UsuarioModel();
        autor.setId(UUID.randomUUID());
        autor.setUsername("autor123");
        autor.setNome("Autor Teste");
        autor.setEmail("autor@email.com");
        autor.setSenha("senha123");
        autor.setTipo(TipoUsuario.ALUNO);
        autor.setCpf("12345678901");
        autor.setCurso("Engenharia");

        usuario = new UsuarioModel();
        usuario.setId(UUID.randomUUID());
        usuario.setUsername("usuario123");
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario@email.com");
        usuario.setSenha("senha123");
        usuario.setTipo(TipoUsuario.ALUNO);
        usuario.setCpf("98765432100");
        usuario.setCurso("Ciência da Computação");

        post = new PostModel();
        post.setId(UUID.randomUUID());
        post.setTitulo("Título do Post");
        post.setConteudo("Conteúdo do post de teste");
        post.setAutor(autor);
        post.setDataCriacao(LocalDateTime.now());
        post.setFavoritos(new HashSet<>());

        postRequestDTO = new PostRequestDTO(
                "Título do Post",
                "Conteúdo do post de teste",
                "autor123"
        );
    }

    @Test
    @DisplayName("Deve criar post com sucesso quando autor existe")
    void deveCriarPostComSucesso() {
        when(usuarioRepository.findByUsername("autor123"))
                .thenReturn(Optional.of(autor));
        when(postRepository.save(any(PostModel.class)))
                .thenReturn(post);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(UUID.class), eq(true)))
                .thenReturn(0L);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(UUID.class), eq(false)))
                .thenReturn(0L);

        PostResponseDTO resultado = postService.criarPost(postRequestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.titulo()).isEqualTo("Título do Post");
        assertThat(resultado.conteudo()).isEqualTo("Conteúdo do post de teste");
        assertThat(resultado.usernameAutor()).isEqualTo("autor123");
        assertThat(resultado.totalCurtidas()).isZero();
        assertThat(resultado.totalDeslikes()).isZero();

        verify(usuarioRepository).findByUsername("autor123");
        verify(postRepository).save(any(PostModel.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não existe ao criar post")
    void deveLancarExcecaoQuandoAutorNaoExiste() {
        when(usuarioRepository.findByUsername("autor_inexistente"))
                .thenReturn(Optional.empty());

        PostRequestDTO dto = new PostRequestDTO(
                "Título",
                "Conteúdo",
                "autor_inexistente"
        );

        assertThatThrownBy(() -> postService.criarPost(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve curtir post pela primeira vez")
    void deveCurtirPostPelaPrimeiraVez() {
        AvaliacaoModel avaliacaoSalva = new AvaliacaoModel();
        avaliacaoSalva.setId(UUID.randomUUID());
        avaliacaoSalva.setUsuario(usuario);
        avaliacaoSalva.setIdConteudo(post.getId());
        avaliacaoSalva.setAvaliacao(true);

        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(usuario.getId(), post.getId()))
                .thenReturn(Optional.empty(), Optional.of(avaliacaoSalva));
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), true))
                .thenReturn(1L);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), false))
                .thenReturn(0L);

        PostResponseDTO resultado = postService.toggleAvaliar(post.getId(), "usuario123", true);

        assertThat(resultado).isNotNull();
        assertThat(resultado.curtidoPeloUsuario()).isTrue();
        assertThat(resultado.descurtidoPeloUsuario()).isFalse();

        verify(avaliacaoRepository).save(any(AvaliacaoModel.class));
        verify(avaliacaoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve remover curtida quando clicar novamente (toggle)")
    void deveRemoverCurtidaQuandoClicarNovamente() {
        AvaliacaoModel avaliacaoExistente = new AvaliacaoModel();
        avaliacaoExistente.setId(UUID.randomUUID());
        avaliacaoExistente.setUsuario(usuario);
        avaliacaoExistente.setIdConteudo(post.getId());
        avaliacaoExistente.setAvaliacao(true);

        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(usuario.getId(), post.getId()))
                .thenReturn(Optional.of(avaliacaoExistente), Optional.empty());
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), true))
                .thenReturn(0L);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), false))
                .thenReturn(0L);

        PostResponseDTO resultado = postService.toggleAvaliar(post.getId(), "usuario123", true);

        assertThat(resultado).isNotNull();
        assertThat(resultado.curtidoPeloUsuario()).isFalse();

        verify(avaliacaoRepository).delete(avaliacaoExistente);
        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve trocar de curtida para descurtida")
    void deveTrocarDeCurtidaParaDescurtida() {
        AvaliacaoModel avaliacaoExistente = new AvaliacaoModel();
        avaliacaoExistente.setId(UUID.randomUUID());
        avaliacaoExistente.setUsuario(usuario);
        avaliacaoExistente.setIdConteudo(post.getId());
        avaliacaoExistente.setAvaliacao(true);

        AvaliacaoModel avaliacaoAtualizada = new AvaliacaoModel();
        avaliacaoAtualizada.setId(avaliacaoExistente.getId());
        avaliacaoAtualizada.setUsuario(usuario);
        avaliacaoAtualizada.setIdConteudo(post.getId());
        avaliacaoAtualizada.setAvaliacao(false);

        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(usuario.getId(), post.getId()))
                .thenReturn(Optional.of(avaliacaoExistente), Optional.of(avaliacaoAtualizada));
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), true))
                .thenReturn(0L);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), false))
                .thenReturn(1L);

        PostResponseDTO resultado = postService.toggleAvaliar(post.getId(), "usuario123", false);

        assertThat(resultado).isNotNull();
        assertThat(resultado.descurtidoPeloUsuario()).isTrue();
        assertThat(resultado.curtidoPeloUsuario()).isFalse();

        verify(avaliacaoRepository).save(avaliacaoExistente);
        verify(avaliacaoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve descurtir post pela primeira vez")
    void deveDescurtirPostPelaPrimeiraVez() {
        AvaliacaoModel avaliacaoSalva = new AvaliacaoModel();
        avaliacaoSalva.setId(UUID.randomUUID());
        avaliacaoSalva.setUsuario(usuario);
        avaliacaoSalva.setIdConteudo(post.getId());
        avaliacaoSalva.setAvaliacao(false);

        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(usuario.getId(), post.getId()))
                .thenReturn(Optional.empty(), Optional.of(avaliacaoSalva));
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), true))
                .thenReturn(0L);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), false))
                .thenReturn(1L);

        PostResponseDTO resultado = postService.toggleAvaliar(post.getId(), "usuario123", false);

        assertThat(resultado).isNotNull();
        assertThat(resultado.descurtidoPeloUsuario()).isTrue();
        assertThat(resultado.curtidoPeloUsuario()).isFalse();

        verify(avaliacaoRepository).save(any(AvaliacaoModel.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando post não existe ao avaliar")
    void deveLancarExcecaoQuandoPostNaoExisteAoAvaliar() {
        UUID postIdInexistente = UUID.randomUUID();
        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findById(postIdInexistente))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.toggleAvaliar(postIdInexistente, "usuario123", true)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post não encontrado");

        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe ao avaliar")
    void deveLancarExcecaoQuandoUsuarioNaoExisteAoAvaliar() {
        when(usuarioRepository.findByUsername("usuario_inexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.toggleAvaliar(post.getId(), "usuario_inexistente", true)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado");

        verify(postRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Deve favoritar post pela primeira vez")
    void deveFavoritarPostPelaPrimeiraVez() {
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.save(any(PostModel.class)))
                .thenReturn(post);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(), anyBoolean()))
                .thenReturn(0L);

        PostResponseDTO resultado = postService.toggleFavorito(post.getId(), "usuario123");

        assertThat(resultado).isNotNull();
        assertThat(post.getFavoritos()).contains(usuario);

        verify(postRepository).save(post);
    }

    @Test
    @DisplayName("Deve desfavoritar post quando já está favoritado")
    void deveDesfavoritarPostQuandoJaEstaFavoritado() {
        post.getFavoritos().add(usuario);

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.save(any(PostModel.class)))
                .thenReturn(post);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(), anyBoolean()))
                .thenReturn(0L);

        PostResponseDTO resultado = postService.toggleFavorito(post.getId(), "usuario123");

        assertThat(resultado).isNotNull();
        assertThat(post.getFavoritos()).doesNotContain(usuario);

        verify(postRepository).save(post);
    }

    @Test
    @DisplayName("Deve lançar exceção quando post não existe ao favoritar")
    void deveLancarExcecaoQuandoPostNaoExisteAoFavoritar() {
        UUID postIdInexistente = UUID.randomUUID();
        when(postRepository.findById(postIdInexistente))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.toggleFavorito(postIdInexistente, "usuario123")
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post não encontrado");

        verify(usuarioRepository, never()).findByUsername(any());
    }

    @Test
    @DisplayName("Deve listar posts com usuário autenticado")
    void deveListarPostsComUsuarioAutenticado() {
        Pageable pageable = PageRequest.of(0, 10);
        List<PostModel> posts = List.of(post);
        Page<PostModel> page = new PageImpl<>(posts, pageable, 1);

        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findAll(pageable))
                .thenReturn(page);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(), anyBoolean()))
                .thenReturn(0L);
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(any(), any()))
                .thenReturn(Optional.empty());

        Page<PostResponseDTO> resultado = postService.listarPosts(pageable, "usuario123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).titulo()).isEqualTo("Título do Post");

        verify(usuarioRepository).findByUsername("usuario123");
        verify(postRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve listar posts sem usuário autenticado")
    void deveListarPostsSemUsuarioAutenticado() {
        Pageable pageable = PageRequest.of(0, 10);
        List<PostModel> posts = List.of(post);
        Page<PostModel> page = new PageImpl<>(posts, pageable, 1);

        when(usuarioRepository.findByUsername(null))
                .thenReturn(Optional.empty());
        when(postRepository.findAll(pageable))
                .thenReturn(page);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(), anyBoolean()))
                .thenReturn(0L);

        Page<PostResponseDTO> resultado = postService.listarPosts(pageable, null);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).curtidoPeloUsuario()).isFalse();
        assertThat(resultado.getContent().get(0).favoritadoPeloUsuario()).isFalse();

        verify(postRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve buscar posts por texto")
    void deveBuscarPostsPorTexto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<PostModel> posts = List.of(post);
        Page<PostModel> page = new PageImpl<>(posts, pageable, 1);

        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.buscarPorTexto(eq("teste"), any(Pageable.class)))
                .thenReturn(page);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(), anyBoolean()))
                .thenReturn(0L);
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(any(), any()))
                .thenReturn(Optional.empty());

        Page<PostResponseDTO> resultado = postService.buscarPosts("teste", pageable, "usuario123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);

        verify(postRepository).buscarPorTexto(eq("teste"), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não encontrar posts na busca")
    void deveRetornarListaVaziaQuandoNaoEncontrarPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostModel> pageVazia = new PageImpl<>(List.of(), pageable, 0);

        when(usuarioRepository.findByUsername(any()))
                .thenReturn(Optional.empty());
        when(postRepository.buscarPorTexto(eq("texto_inexistente"), any(Pageable.class)))
                .thenReturn(pageVazia);

        Page<PostResponseDTO> resultado = postService.buscarPosts("texto_inexistente", pageable, null);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).isEmpty();
        assertThat(resultado.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("Deve listar favoritos do usuário")
    void deveListarFavoritosDoUsuario() {
        Pageable pageable = PageRequest.of(0, 10);
        post.getFavoritos().add(usuario);
        List<PostModel> favoritos = List.of(post);
        Page<PostModel> page = new PageImpl<>(favoritos, pageable, 1);

        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findByFavoritosContaining(usuario, pageable))
                .thenReturn(page);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(any(), anyBoolean()))
                .thenReturn(0L);
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(any(), any()))
                .thenReturn(Optional.empty());

        Page<PostResponseDTO> resultado = postService.listarFavoritos("usuario123", pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).favoritadoPeloUsuario()).isTrue();

        verify(postRepository).findByFavoritosContaining(usuario, pageable);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe ao listar favoritos")
    void deveLancarExcecaoQuandoUsuarioNaoExisteAoListarFavoritos() {
        Pageable pageable = PageRequest.of(0, 10);
        when(usuarioRepository.findByUsername("usuario_inexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.listarFavoritos("usuario_inexistente", pageable)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado");

        verify(postRepository, never()).findByFavoritosContaining(any(), any());
    }

    @Test
    @DisplayName("Deve contar curtidas e descurtidas corretamente")
    void deveContarCurtidasEDescurtidasCorretamente() {
        when(usuarioRepository.findByUsername("usuario123"))
                .thenReturn(Optional.of(usuario));
        when(postRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(post)));
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), true))
                .thenReturn(5L);
        when(avaliacaoRepository.countByIdConteudoAndAvaliacao(post.getId(), false))
                .thenReturn(2L);
        when(avaliacaoRepository.findByUsuarioIdAndIdConteudo(any(), any()))
                .thenReturn(Optional.empty());

        Page<PostResponseDTO> resultado = postService.listarPosts(PageRequest.of(0, 10), "usuario123");

        PostResponseDTO postDTO = resultado.getContent().get(0);
        assertThat(postDTO.totalCurtidas()).isEqualTo(5);
        assertThat(postDTO.totalDeslikes()).isEqualTo(2);

        verify(avaliacaoRepository).countByIdConteudoAndAvaliacao(post.getId(), true);
        verify(avaliacaoRepository).countByIdConteudoAndAvaliacao(post.getId(), false);
    }
}