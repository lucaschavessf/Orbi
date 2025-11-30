package com.example.orbi.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orbi.dto.DominioRequestDTO;
import com.example.orbi.dto.DominioResponseDTO;
import com.example.orbi.dto.InstituicaoRequestDTO;
import com.example.orbi.dto.InstituicaoResponseDTO;
import com.example.orbi.services.InstituicaoService;
import com.example.orbi.services.PostService;

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

    @PostMapping("/dominio/{instituicao}")
    public ResponseEntity<DominioResponseDTO> criarDominio(
            @PathVariable String instituicao,
            @RequestBody @Valid DominioRequestDTO dto) {
        
        DominioResponseDTO response = instituicaoService.criarDominio(instituicao, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
}
