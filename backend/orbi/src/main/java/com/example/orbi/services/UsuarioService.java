package com.example.orbi.services;

import org.springframework.stereotype.Service;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        UsuarioModel model = usuarioDTO.toModel();
        UsuarioModel saved = usuarioRepository.save(model);
        return new UsuarioDTO(saved);
    }



    
}
