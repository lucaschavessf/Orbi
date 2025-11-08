package com.example.orbi.repositories;

import com.example.orbi.models.AvaliacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoModel, UUID> {

    Optional<AvaliacaoModel> findByUsuarioIdAndIdConteudo(UUID usuarioId, UUID idConteudo);

    long countByIdConteudoAndAvaliacao(UUID idConteudo, Boolean avaliacao);
}

