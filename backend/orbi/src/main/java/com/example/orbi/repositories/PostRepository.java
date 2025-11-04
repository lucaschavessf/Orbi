package com.example.orbi.repositories;

import com.example.orbi.models.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostModel, UUID> , CustomPostRepository {
    @Query("SELECT COUNT(u) FROM PostModel p JOIN p.curtidas u WHERE p.id = :postId")
    long contarCurtidas(@Param("postId") UUID postId);

}
