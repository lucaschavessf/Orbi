package com.example.orbi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Map<String, CodeEntry> codes = new ConcurrentHashMap<>();

    private static class CodeEntry {
        final String code;
        final Instant created;
        CodeEntry(String code) {
            this.code = code;
            this.created = Instant.now();
        }
    }

    public String sendVerificationCode(String email) {
        String normalized = normalize(email);
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        codes.put(normalized, new CodeEntry(code));

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(normalized);
        msg.setSubject("Seu código de verificação");
        msg.setText("Seu código é: " + code);
        try {
            mailSender.send(msg);
            System.out.println("Email enviado para " + normalized + " com código: " + code);
        } catch (Exception e) {
            System.out.println("Falha ao enviar email (mas código salvo): " + e.getMessage());
            System.out.println("Código (teste) para " + normalized + ": " + code);
        }
        return code;
    }

    public boolean verifyCode(String email, String code) {
        String normalized = normalize(email);
        CodeEntry entry = codes.get(normalized);
        System.out.println("VERIFY -> normalized: " + normalized + " | entry: " + (entry == null ? "null" : entry.code));
        if (entry == null) return false;

        if (Instant.now().isAfter(entry.created.plusSeconds(300))) {
            codes.remove(normalized);
            System.out.println("Código expirado para " + normalized);
            return false;
        }

        boolean ok = entry.code.equals(code);
        if (ok) {
            codes.remove(normalized);
            System.out.println("Código verificado com sucesso para " + normalized);
        } else {
            System.out.println("Código inválido para " + normalized);
        }
        return ok;
    }

    private String normalize(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase();
    }
}
