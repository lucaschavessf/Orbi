package com.example.orbi.services;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.models.enums.TipoUsuario;
import com.example.orbi.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - Testes Unitários")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;
    private UsuarioModel usuarioModel;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("joao123");
        usuarioDTO.setNome("João Silva");
        usuarioDTO.setCpf("12345678901");
        usuarioDTO.setEmail("joao@email.com");
        usuarioDTO.setSenha("senha123");
        usuarioDTO.setTipo(TipoUsuario.ALUNO);
        usuarioDTO.setCurso("Engenharia de Software");

        usuarioModel = new UsuarioModel();
        usuarioModel.setId(UUID.randomUUID());
        usuarioModel.setUsername("joao123");
        usuarioModel.setNome("João Silva");
        usuarioModel.setCpf("12345678901");
        usuarioModel.setEmail("joao@email.com");
        usuarioModel.setSenha("senhaCriptografada123");
        usuarioModel.setTipo(TipoUsuario.ALUNO);
        usuarioModel.setCurso("Engenharia de Software");
    }


    @Test
    @DisplayName("Deve criar usuário com sucesso quando dados são válidos")
    void deveCriarUsuarioComSucesso() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada123");
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuarioModel);

        UsuarioDTO resultado = usuarioService.createUsuario(usuarioDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo("joao123");
        assertThat(resultado.getEmail()).isEqualTo("joao@email.com");
        assertThat(resultado.getSenha()).isNull();

        verify(usuarioRepository).findByUsername("joao123");
        verify(usuarioRepository).findByEmail("joao@email.com");
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(any(UsuarioModel.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando username já existe")
    void deveLancarExcecaoQuandoUsernameJaExiste() {
        when(usuarioRepository.findByUsername("joao123"))
                .thenReturn(Optional.of(usuarioModel));

        assertThatThrownBy(() -> usuarioService.createUsuario(usuarioDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username já está em uso");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("joao@email.com"))
                .thenReturn(Optional.of(usuarioModel));

        assertThatThrownBy(() -> usuarioService.createUsuario(usuarioDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email já está em uso");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve criptografar senha antes de salvar")
    void deveCriptografarSenhaAntesDeSalvar() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuarioModel);

        usuarioService.createUsuario(usuarioDTO);

        verify(passwordEncoder).encode("senha123");
    }

    @Test
    @DisplayName("Deve buscar usuário por username com sucesso")
    void deveBuscarUsuarioPorUsernameComSucesso() {
        when(usuarioRepository.findByUsername("joao123"))
                .thenReturn(Optional.of(usuarioModel));

        UsuarioDTO resultado = usuarioService.buscarPorUsername("joao123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo("joao123");
        assertThat(resultado.getSenha()).isNull();

        verify(usuarioRepository).findByUsername("joao123");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado na busca")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByUsername("inexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorUsername("inexistente"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve fazer login com sucesso quando credenciais corretas")
    void deveFazerLoginComSucesso() {
        when(usuarioRepository.findByUsername("joao123"))
                .thenReturn(Optional.of(usuarioModel));
        when(passwordEncoder.matches("senha123", "senhaCriptografada123"))
                .thenReturn(true);

        UsuarioDTO resultado = usuarioService.verificarLogin("joao123", "senha123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo("joao123");
        assertThat(resultado.getSenha()).isNull();

        verify(passwordEncoder).matches("senha123", "senhaCriptografada123");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha incorreta")
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        when(usuarioRepository.findByUsername("joao123"))
                .thenReturn(Optional.of(usuarioModel));
        when(passwordEncoder.matches("senhaErrada", "senhaCriptografada123"))
                .thenReturn(false);

        assertThatThrownBy(() ->
                usuarioService.verificarLogin("joao123", "senhaErrada")
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário ou senha inválidos");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe no login")
    void deveLancarExcecaoQuandoUsuarioNaoExisteNoLogin() {
        when(usuarioRepository.findByUsername("inexistente"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                usuarioService.verificarLogin("inexistente", "senha123")
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário ou senha inválidos");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve listar todos os usuários sem expor senhas")
    void deveListarUsuariosSemSenhas() {
        UsuarioModel usuario2 = new UsuarioModel();
        usuario2.setUsername("maria456");
        usuario2.setSenha("outraSenha");

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuarioModel, usuario2));

        List<UsuarioDTO> resultado = usuarioService.listarUsuarios();

        assertThat(resultado).hasSize(2);
        assertThat(resultado)
                .extracting(UsuarioDTO::getSenha)
                .containsOnly((String) null);
    }


    @Test
    @DisplayName("Deve tratar username null adequadamente")
    void deveTratarUsernameNull() {
        usuarioDTO.setUsername(null);

        assertThatThrownBy(() -> usuarioService.createUsuario(usuarioDTO))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Deve tratar email vazio adequadamente")
    void deveTratarEmailVazio() {
        usuarioDTO.setEmail("");
    }
}