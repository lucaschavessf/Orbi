package com.example.orbi.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar_usuario")
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO createdUsuario = usuarioService.createUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
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

    
}
