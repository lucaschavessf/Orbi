package com.example.orbi.controllers;

import com.example.orbi.dto.LoginResponseDTO;
import com.example.orbi.services.JwtService;

import org.springframework.web.bind.annotation.*;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.dto.LoginRequestDTO;
import com.example.orbi.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO createdUsuario = usuarioService.createUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String username) {
        try {
            UsuarioDTO usuario = usuarioService.buscarPorUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deletarUsuarioPorUsername(@PathVariable String username) {
        try {
            usuarioService.deleteUsuarioPorUsername(username);
            return ResponseEntity.ok("Usuário deletado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> verificarUsuario(@RequestBody LoginRequestDTO loginRequest) {
        try {
            UsuarioDTO usuario = usuarioService.verificarLogin(
                    loginRequest.getUsername(),
                    loginRequest.getSenha()
            );

            String token = jwtService.generateToken(usuario.getUsername(), usuario.getTipo().name());
            Long expiresIn = jwtService.getExpirationTime();

            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    usuario.getUsername(),
                    usuario.getTipo().name(),
                    expiresIn
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}
