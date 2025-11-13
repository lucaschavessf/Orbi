package com.example.orbi.controllers;

import com.example.orbi.dto.ComentarioRequestDTO;
import com.example.orbi.dto.ComentarioResponseDTO;
import com.example.orbi.services.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> criarComentario(@RequestBody @Valid ComentarioRequestDTO dto) {
        try {
            ComentarioResponseDTO response = comentarioService.criarComentario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentarios(
            @PathVariable UUID postId,
            @RequestParam String username) {
        try {
            List<ComentarioResponseDTO> comentarios = comentarioService.listarComentariosPorPost(postId, username);
            return ResponseEntity.ok(comentarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{comentarioId}/avaliar")
    public ResponseEntity<ComentarioResponseDTO> avaliarComentario(
            @PathVariable UUID comentarioId,
            @RequestParam Boolean avaliacao,
            @RequestParam String username) {
        try {
            ComentarioResponseDTO response = comentarioService.avaliarComentario(comentarioId, username, avaliacao);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{comentarioId}")
    public ResponseEntity<Void> deletarComentario(
            @PathVariable UUID comentarioId,
            @RequestParam String username) {
        try {
            comentarioService.deletarComentario(comentarioId, username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}