package com.example.orbi.repositories;

import com.example.orbi.models.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<PostModel> buscarPorTexto(String texto, Pageable pageable);
}

