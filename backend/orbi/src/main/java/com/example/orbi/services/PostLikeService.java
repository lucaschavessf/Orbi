// language: java
package com.example.orbi.services;

import com.example.orbi.models.PostModel;
import com.example.orbi.models.UsuarioModel;
import com.example.orbi.repositories.PostRepository;
import com.example.orbi.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class PostLikeService {

    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    public PostLikeService(PostRepository postRepository, UsuarioRepository usuarioRepository) {
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void toggleLike(UUID postId, String username) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        UsuarioModel user = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioModel managedUser = usuarioRepository.getReferenceById(user.getId());

        Set<UsuarioModel> curtidas = post.getCurtidas();
        Set<UsuarioModel> deslikes = post.getDeslikes();

        if (curtidas.contains(managedUser)) {
            curtidas.remove(managedUser);
        } else {
            deslikes.remove(managedUser);
            curtidas.add(managedUser);
        }

        postRepository.save(post);
    }
}
