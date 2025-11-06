package com.example.orbi.controllers;

import org.springframework.web.bind.annotation.*;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.dto.LoginRequestDTO;
import com.example.orbi.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar_usuario")
    public ResponseEntity<?> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO createdUsuario = usuarioService.createUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/listar_usuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/deletar/username/{username}")
    public ResponseEntity<String> deletarUsuarioPorUsername(@PathVariable String username) {
        try {
            usuarioService.deleteUsuarioPorUsername(username);
            return ResponseEntity.ok("Usuário deletado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String username) {
        try {
            UsuarioDTO usuario = usuarioService.buscarPorUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> verificarUsuario(@RequestBody LoginRequestDTO loginRequest) {
        try {
            UsuarioDTO usuario = usuarioService.verificarLogin(
                    loginRequest.getUsername(),
                    loginRequest.getSenha()
            );
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}
