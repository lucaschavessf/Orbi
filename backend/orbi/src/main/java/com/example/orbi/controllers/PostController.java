package com.example.orbi.controllers;

import com.example.orbi.dto.PageResponseDTO;
import com.example.orbi.dto.PostRequestDTO;
import com.example.orbi.dto.PostResponseDTO;
import com.example.orbi.services.PostService;
import com.example.orbi.services.PostDislikeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private com.example.orbi.services.PostLikeService postLikeService;

    @Autowired
    private PostDislikeService postDislikeService;

    @PostMapping("/criar_post")
    public ResponseEntity<PostResponseDTO> criarPost(@RequestBody @Valid PostRequestDTO dto) {
        PostResponseDTO response = postService.criarPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/listar_posts")
    public ResponseEntity<PageResponseDTO<PostResponseDTO>> listarPosts(
            @RequestParam String username,
            @PageableDefault(size = 2, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PostResponseDTO> page = postService.listarPosts(pageable,username);
        return ResponseEntity.ok(PageResponseDTO.of(page));
    }

    @PostMapping("/{id}/curtir")
    public ResponseEntity<String> curtirPost(@PathVariable("id") UUID id,
                                             @RequestParam String username) {
        try {
            postLikeService.toggleLike(id, username);
            return ResponseEntity.ok("Curtida atualizada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/descurtir")
    public ResponseEntity<String> descurtirPost(@PathVariable("id") UUID id,
                                                @RequestParam String username) {
        try {
            postDislikeService.toggleDislike(id, username);
            return ResponseEntity.ok("Deslike atualizado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
