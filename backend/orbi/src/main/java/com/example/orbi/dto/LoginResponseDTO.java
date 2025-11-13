package com.example.orbi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String type = "Bearer";
    private String username;
    private String tipo;
    private Long expiresIn;

    public LoginResponseDTO(String token, String username, String tipo, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.tipo = tipo;
        this.expiresIn = expiresIn;
    }
}