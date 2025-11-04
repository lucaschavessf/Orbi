package com.example.orbi.repositories;

import com.example.orbi.models.PostModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<PostModel> buscarPorTexto(String texto, Pageable pageable) {
        // Preparar o texto para busca (case-insensitive e com wildcards)
        String searchPattern = "%" + texto.toLowerCase() + "%";

        // Query JPQL que busca em título, conteúdo, username e nome do autor
        String jpql = "SELECT DISTINCT p FROM PostModel p " +
                      "LEFT JOIN FETCH p.autor a " +
                      "LEFT JOIN FETCH p.curtidas " +
                      "WHERE LOWER(p.titulo) LIKE :searchPattern " +
                      "OR LOWER(p.conteudo) LIKE :searchPattern " +
                      "OR LOWER(a.username) LIKE :searchPattern " +
                      "OR LOWER(a.nome) LIKE :searchPattern " +
                      "ORDER BY p.dataCriacao DESC";

        // Query para contar total de resultados
        String countJpql = "SELECT COUNT(DISTINCT p) FROM PostModel p " +
                          "LEFT JOIN p.autor a " +
                          "WHERE LOWER(p.titulo) LIKE :searchPattern " +
                          "OR LOWER(p.conteudo) LIKE :searchPattern " +
                          "OR LOWER(a.username) LIKE :searchPattern " +
                          "OR LOWER(a.nome) LIKE :searchPattern";

        // Executar query principal
        TypedQuery<PostModel> query = entityManager.createQuery(jpql, PostModel.class);
        query.setParameter("searchPattern", searchPattern);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<PostModel> posts = query.getResultList();

        // Contar total de resultados
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        countQuery.setParameter("searchPattern", searchPattern);
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(posts, pageable, total);
    }
}

