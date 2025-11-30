package com.example.orbi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.orbi.models.DominioModel;

@Repository
public interface DominioRepository extends JpaRepository<DominioModel, UUID> { 
    Boolean existsByDominioIgnoreCase(String dominio);
}

