package com.example.orbi.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.orbi.models.DominioModel;
import com.example.orbi.models.InstituicaoModel;

@Repository
public interface DominioRepository extends JpaRepository<DominioModel, UUID> {
    Boolean existsByDominioIgnoreCase(String dominio);
    Optional<DominioModel> findByDominioIgnoreCase(String dominio);

    @Query("SELECT d FROM DominioModel d WHERE d.id_instituicao = :instituicao")
    List<DominioModel> findByInstituicao(@Param("instituicao") InstituicaoModel instituicao);
}