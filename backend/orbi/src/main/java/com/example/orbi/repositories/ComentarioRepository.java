package com.example.orbi.repositories;

import com.example.orbi.models.ComentarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioModel, UUID> {

    @Query("SELECT c FROM ComentarioModel c WHERE c.post.id = :postId AND c.comentarioPai IS NULL")
    List<ComentarioModel> findByPostIdAndComentarioPaiIsNull(@Param("postId") UUID postId);

    @Query("SELECT c FROM ComentarioModel c WHERE c.comentarioPai.id = :comentarioPaiId ORDER BY c.dataCriacao ASC")
    List<ComentarioModel> findByComentarioPaiIdOrderByDataCriacaoAsc(@Param("comentarioPaiId") UUID comentarioPaiId);

    long countByPostId(UUID postId);

    @Query("SELECT COUNT(c) FROM ComentarioModel c WHERE c.comentarioPai.id = :comentarioId")
    long countRespostasByComentarioId(@Param("comentarioId") UUID comentarioId);

    @Query("SELECT c FROM ComentarioModel c WHERE c.post.id = :postId")
    List<ComentarioModel> findAllByPostId(@Param("postId") UUID postId);

    @Modifying
    @Query("DELETE FROM ComentarioModel c WHERE c.post.id = :postId")
    void deleteByPostId(@Param("postId") UUID postId);
}