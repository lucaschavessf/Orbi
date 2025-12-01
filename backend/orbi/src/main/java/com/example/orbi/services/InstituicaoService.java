package com.example.orbi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orbi.dto.InstituicaoRequestDTO;
import com.example.orbi.dto.InstituicaoResponseDTO;
import com.example.orbi.models.DominioModel;
import com.example.orbi.models.InstituicaoModel;
import com.example.orbi.repositories.DominioRepository;
import com.example.orbi.repositories.InstituicaoRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private DominioRepository dominioRepository;

    @Transactional
    public InstituicaoResponseDTO criarInstituicao(InstituicaoRequestDTO dto) {
        // Criar instituição
        InstituicaoModel instituicao = new InstituicaoModel();
        instituicao.setNome(dto.nome());
        InstituicaoModel savedInstituicao = instituicaoRepository.save(instituicao);

        // Criar domínios associados
        if (dto.dominios() != null && !dto.dominios().isEmpty()) {
            for (String dominio : dto.dominios()) {
                DominioModel dominioModel = new DominioModel();
                dominioModel.setDominio(dominio.trim().toLowerCase());
                dominioModel.setId_instituicao(savedInstituicao);
                dominioRepository.save(dominioModel);
            }
        }

        // Buscar domínios salvos para retornar
        List<String> dominiosSalvos = dominioRepository.findByInstituicao(savedInstituicao)
                .stream()
                .map(DominioModel::getDominio)
                .collect(Collectors.toList());

        return new InstituicaoResponseDTO(
                savedInstituicao.getId(),
                savedInstituicao.getNome(),
                dominiosSalvos
        );
    }

    public List<InstituicaoResponseDTO> listarInstituicoes() {
        return instituicaoRepository.findAll().stream()
                .map(instituicao -> {
                    List<String> dominios = dominioRepository.findByInstituicao(instituicao)
                            .stream()
                            .map(DominioModel::getDominio)
                            .collect(Collectors.toList());

                    return new InstituicaoResponseDTO(
                            instituicao.getId(),
                            instituicao.getNome(),
                            dominios
                    );
                })
                .collect(Collectors.toList());
    }

    public InstituicaoResponseDTO buscarInstituicaoPorId(String id) {
        InstituicaoModel instituicao = instituicaoRepository.findById(java.util.UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        List<String> dominios = dominioRepository.findByInstituicao(instituicao)
                .stream()
                .map(DominioModel::getDominio)
                .collect(Collectors.toList());

        return new InstituicaoResponseDTO(
                instituicao.getId(),
                instituicao.getNome(),
                dominios
        );
    }
}