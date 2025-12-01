package com.example.orbi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.orbi.dto.InstituicaoRequestDTO;
import com.example.orbi.dto.InstituicaoResponseDTO;
import com.example.orbi.services.InstituicaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/instituicao")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InstituicaoController {

    @Autowired
    private InstituicaoService instituicaoService;

    @PostMapping
    public ResponseEntity<InstituicaoResponseDTO> criarInstituicao(@RequestBody @Valid InstituicaoRequestDTO dto) {
        InstituicaoResponseDTO response = instituicaoService.criarInstituicao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoResponseDTO>> listarInstituicoes() {
        List<InstituicaoResponseDTO> instituicoes = instituicaoService.listarInstituicoes();
        return ResponseEntity.ok(instituicoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoResponseDTO> buscarInstituicao(@PathVariable String id) {
        try {
            InstituicaoResponseDTO instituicao = instituicaoService.buscarInstituicaoPorId(id);
            return ResponseEntity.ok(instituicao);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}