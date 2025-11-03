package com.example.orbi.controllers;

import com.example.orbi.dto.BuscaResponseDTO;
import com.example.orbi.services.BuscaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/busca")
@CrossOrigin(origins = "*")
public class BuscaController {

    @Autowired
    private BuscaService buscaService;

    @GetMapping
    public ResponseEntity<Page<BuscaResponseDTO>> buscar(
            @RequestParam(name = "q") String texto,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Page<BuscaResponseDTO> resultados = buscaService.buscarPosts(texto.trim(), page, size);
        return ResponseEntity.ok(resultados);
    }
}

