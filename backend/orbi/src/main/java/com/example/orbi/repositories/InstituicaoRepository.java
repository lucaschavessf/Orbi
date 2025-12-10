package com.example.orbi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.orbi.models.InstituicaoModel;

@Repository
public interface InstituicaoRepository extends JpaRepository<InstituicaoModel, UUID>{
    Optional<InstituicaoModel> findByNomeIgnoreCase(String nome);

}
