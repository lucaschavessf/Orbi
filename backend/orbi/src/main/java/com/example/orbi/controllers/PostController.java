package com.example.orbi.controllers;

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
            @PageableDefault(size = 2, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PostResponseDTO> page = postService.listarPosts(pageable);
        return ResponseEntity.ok(PageResponseDTO.of(page));
    }
}
