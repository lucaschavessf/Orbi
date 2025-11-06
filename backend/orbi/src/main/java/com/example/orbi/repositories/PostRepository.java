package com.example.orbi.repositories;

import com.example.orbi.models.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostModel, UUID> {

    @Query(value = """
        SELECT DISTINCT p.* FROM posts p 
        LEFT JOIN usuarios a ON p.autor_id = a.id 
        WHERE 
            to_tsvector('portuguese', COALESCE(p.titulo, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR to_tsvector('portuguese', COALESCE(p.conteudo, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR to_tsvector('portuguese', COALESCE(a.username, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR to_tsvector('portuguese', COALESCE(a.nome, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(p.conteudo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(a.username) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
        ORDER BY p.data_criacao DESC
        """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id) FROM posts p 
        LEFT JOIN usuarios a ON p.autor_id = a.id 
        WHERE 
            to_tsvector('portuguese', COALESCE(p.titulo, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR to_tsvector('portuguese', COALESCE(p.conteudo, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR to_tsvector('portuguese', COALESCE(a.username, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR to_tsvector('portuguese', COALESCE(a.nome, '')) @@ to_tsquery('portuguese', :texto || ':*')
            OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(p.conteudo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(a.username) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
        """,
            nativeQuery = true)
    Page<PostModel> buscarPorTexto(@Param("texto") String texto, Pageable pageable);
}
