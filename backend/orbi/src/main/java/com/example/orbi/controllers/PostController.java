package com.example.orbi.controllers;

import java.util.UUID;

import com.example.orbi.dto.PageResponseDTO;
import com.example.orbi.dto.PostRequestDTO;
import com.example.orbi.dto.PostResponseDTO;
import com.example.orbi.services.PostService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> criarPost(@RequestBody @Valid PostRequestDTO dto) {
        PostResponseDTO response = postService.criarPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<PostResponseDTO>> listarPosts(
            @RequestParam String username,
            @PageableDefault(size = 2, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PostResponseDTO> page = postService.listarPosts(pageable, username);
        return ResponseEntity.ok(PageResponseDTO.of(page));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<PostResponseDTO>> buscarPosts(
            @RequestParam(name = "q") String texto,
            @RequestParam(name = "username", required = false) String username,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable) {
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Page<PostResponseDTO> resultados = postService.buscarPosts(texto.trim(), pageable, username);
        return ResponseEntity.ok(PageResponseDTO.of(resultados)); // ✅ Retorna resultados reais!
    }

    @PostMapping("/{postId}/avaliar")
    public ResponseEntity<PostResponseDTO> avaliarPost(
            @PathVariable UUID postId,
            @RequestParam Boolean avaliacao,
            @RequestParam String username) {

        PostResponseDTO dto = postService.toggleAvaliar(postId, username, avaliacao);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/favoritar")
    public ResponseEntity<PostResponseDTO> favoritarPost(
            @PathVariable("id") UUID id,
            @RequestParam String username) {
        try {
            System.out.println("inicio");
            PostResponseDTO response = postService.toggleFavorito(id, username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/favoritos")
    public ResponseEntity<PageResponseDTO<PostResponseDTO>> listarFavoritos(
            @RequestParam String username,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable) {
        try {
            Page<PostResponseDTO> favoritos = postService.listarFavoritos(username, pageable);
            return ResponseEntity.ok(PageResponseDTO.of(favoritos));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
