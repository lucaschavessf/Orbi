package com.example.orbi.services;

import com.example.orbi.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PreRegistroService {

    private static final Map<String, PreRegistroEntry> preRegistros = new ConcurrentHashMap<>();

    private static class PreRegistroEntry {
        final UsuarioDTO usuarioDTO;
        final Instant created;

        PreRegistroEntry(UsuarioDTO usuarioDTO) {
            this.usuarioDTO = usuarioDTO;
            this.created = Instant.now();
        }
    }

    public void salvarPreRegistro(String email, UsuarioDTO usuarioDTO) {
        String normalized = normalize(email);
        preRegistros.put(normalized, new PreRegistroEntry(usuarioDTO));
        System.out.println("✅ Pré-registro salvo para: " + normalized);
    }

    public UsuarioDTO obterPreRegistro(String email) {
        String normalized = normalize(email);
        PreRegistroEntry entry = preRegistros.get(normalized);

        if (entry == null) {
            System.out.println("❌ Nenhum pré-registro encontrado para: " + normalized);
            return null;
        }

        if (Instant.now().isAfter(entry.created.plusSeconds(900))) {
            preRegistros.remove(normalized);
            System.out.println("⏰ Pré-registro expirado para: " + normalized);
            return null;
        }

        return entry.usuarioDTO;
    }

    public void removerPreRegistro(String email) {
        String normalized = normalize(email);
        preRegistros.remove(normalized);
        System.out.println("🗑️ Pré-registro removido para: " + normalized);
    }

    public boolean existePreRegistro(String email) {
        String normalized = normalize(email);
        PreRegistroEntry entry = preRegistros.get(normalized);

        if (entry == null) {
            return false;
        }

        if (Instant.now().isAfter(entry.created.plusSeconds(900))) {
            preRegistros.remove(normalized);
            return false;
        }

        return true;
    }

    private String normalize(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase();
    }
}