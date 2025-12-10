package com.example.orbi.repositories;

import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostModel, UUID> {

    Page<PostModel> findByAutor_Instituicao_Id(UUID instituicaoId, Pageable pageable);

    @Query(value = """
    SELECT DISTINCT p.* FROM posts p 
    LEFT JOIN usuarios a ON p.autor_id = a.id 
    WHERE a.instituicao_id = :instituicaoId
    AND (
        unaccent(LOWER(p.titulo)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
        OR unaccent(LOWER(p.conteudo)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
        OR unaccent(LOWER(a.username)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
        OR unaccent(LOWER(a.nome)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
    )
    ORDER BY p.data_criacao DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT p.id) FROM posts p 
    LEFT JOIN usuarios a ON p.autor_id = a.id 
    WHERE a.instituicao_id = :instituicaoId
    AND (
        unaccent(LOWER(p.titulo)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
        OR unaccent(LOWER(p.conteudo)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
        OR unaccent(LOWER(a.username)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
        OR unaccent(LOWER(a.nome)) LIKE unaccent(LOWER(CONCAT('%', :texto, '%')))
    )
    """,
            nativeQuery = true)
    Page<PostModel> buscarPorTextoEInstituicao(@Param("texto") String texto, @Param("instituicaoId") UUID instituicaoId, Pageable pageable);
    Page<PostModel> findByFavoritosContainingAndAutor_Instituicao_Id(UsuarioModel usuario, UUID instituicaoId, Pageable pageable);
    Page<PostModel> findByAutor_Id(UUID autorId, Pageable pageable);
    Page<PostModel> findByFavoritosContaining(UsuarioModel usuario, Pageable pageable);
}
