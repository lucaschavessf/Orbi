package com.example.orbi.repositories;

import com.example.orbi.models.AvaliacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoModel, UUID> {

    Optional<AvaliacaoModel> findByUsuarioIdAndIdConteudo(UUID usuarioId, UUID idConteudo);

    long countByIdConteudoAndAvaliacao(UUID idConteudo, Boolean avaliacao);

    @Modifying
    @Query("DELETE FROM AvaliacaoModel a WHERE a.idConteudo = :idConteudo")
    void deleteByIdConteudo(@Param("idConteudo") UUID idConteudo);
}