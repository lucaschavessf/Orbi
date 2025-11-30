package com.example.orbi.controllers;

import com.example.orbi.dto.UsuarioDTO;
import com.example.orbi.services.EmailService;
import com.example.orbi.services.PreRegistroService;
import com.example.orbi.services.UsuarioService;
import com.example.orbi.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PreRegistroService preRegistroService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/iniciar-registro")
    public ResponseEntity<?> iniciarRegistro(@RequestBody UsuarioDTO usuarioDTO) {
        try {

            if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body("Email é obrigatório");
            }

            if (usuarioDTO.getUsername() == null || usuarioDTO.getUsername().isBlank()) {
                return ResponseEntity.badRequest().body("Username é obrigatório");
            }

            if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().isBlank()) {
                return ResponseEntity.badRequest().body("Senha é obrigatória");
            }

            if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email já está cadastrado");
            }

            if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username já está em uso");
            }

            preRegistroService.salvarPreRegistro(usuarioDTO.getEmail(), usuarioDTO);
            emailService.sendVerificationCode(usuarioDTO.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Código de verificação enviado para " + usuarioDTO.getEmail());
            response.put("email", usuarioDTO.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao iniciar registro: " + e.getMessage());
        }
    }

    @PostMapping("/verificar-e-criar")
    public ResponseEntity<?> verificarECriar(@RequestParam String email, @RequestParam String code) {
        try {
            if (!emailService.verifyCode(email, code)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Código inválido ou expirado");
            }

            UsuarioDTO usuarioDTO = preRegistroService.obterPreRegistro(email);

            if (usuarioDTO == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Dados de registro não encontrados ou expiraram. Por favor, inicie o registro novamente.");
            }

            UsuarioDTO usuarioCriado = usuarioService.createUsuario(usuarioDTO);

            preRegistroService.removerPreRegistro(email);

            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Usuário criado com sucesso!");
            response.put("usuario", usuarioCriado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Erro ao criar usuário: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao verificar código: " + e.getMessage());
        }
    }

    @PostMapping("/reenviar-codigo")
    public ResponseEntity<?> reenviarCodigo(@RequestParam String email) {
        try {

            if (!preRegistroService.existePreRegistro(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Nenhum registro pendente encontrado para este email");
            }
            emailService.sendVerificationCode(email);

            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Código reenviado para " + email);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao reenviar código: " + e.getMessage());
        }
    }

    @PostMapping("/cancelar-registro")
    public ResponseEntity<?> cancelarRegistro(@RequestParam String email) {
        try {
            preRegistroService.removerPreRegistro(email);

            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Registro cancelado");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cancelar registro: " + e.getMessage());
        }
    }
}