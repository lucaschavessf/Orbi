package com.example.orbi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orbi.dto.DominioRequestDTO;
import com.example.orbi.dto.DominioResponseDTO;
import com.example.orbi.dto.InstituicaoRequestDTO;
import com.example.orbi.dto.InstituicaoResponseDTO;
import com.example.orbi.models.DominioModel;
import com.example.orbi.models.InstituicaoModel;
import com.example.orbi.repositories.DominioRepository;
import com.example.orbi.repositories.InstituicaoRepository;

import jakarta.transaction.Transactional;

@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private DominioRepository dominioRepository;


    @Transactional
    public InstituicaoResponseDTO criarInstituicao(InstituicaoRequestDTO dto){

        InstituicaoModel model = new InstituicaoModel();
        model.setNome(dto.nome());

        InstituicaoModel saved = instituicaoRepository.save(model);

        return new InstituicaoResponseDTO(
                saved.getId(),
                saved.getNome()
        );
    }

    @Transactional
    public DominioResponseDTO criarDominio(String nomeInstituicao, DominioRequestDTO dto) {

        InstituicaoModel instituicao = instituicaoRepository
                .findByNomeIgnoreCase(nomeInstituicao.trim())
                .orElseThrow(() -> new RuntimeException(
                        "Instituição não encontrada: " + nomeInstituicao));

        DominioModel dominio = new DominioModel();
        dominio.setDominio(dto.dominio());
        dominio.setId_instituicao(instituicao);

        DominioModel saved = dominioRepository.save(dominio);

        return new DominioResponseDTO(
                saved.getId(),
                saved.getDominio(),
                saved.getId_instituicao().getNome()
        );
    }

}

